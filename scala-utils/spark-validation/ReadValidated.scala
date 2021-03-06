import io.circe._
import io.circe.parser._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.types._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

sealed trait Contract {
  val dateFormat: Option[String]
  val keyConverter: Option[KeyConverter]
}

case class JSON(dateFormat: Option[String] = None,
                keyConverter: Option[KeyConverter] = None) extends Contract

case class CSV(hasHeader: Boolean,
               quotedStrings: Boolean,
               ignoreAdditional: Boolean = true,
               dateFormat: Option[String] = None,
               keyConverter: Option[KeyConverter] = None) extends Contract

// I hate Either, want to use Xor from cats, or \/ from Scalaz instead

sealed trait NotProcessableReasonType

case object IncorrectType extends NotProcessableReasonType

case object MissingField extends NotProcessableReasonType

case object InvalidTimestamp extends NotProcessableReasonType

case object InvalidJSON extends NotProcessableReasonType

// Could have a second case class without recordLine and populate it later, would mean we don't have to keep passing it down
case class NotProcessableRecordTyped(recordLine: String,
                                     notProcessableReasonType: NotProcessableReasonType,
                                     notProcessableReasonMessage: String,
                                     stackTrace: Option[String])

case class NotProcessableRecord(recordLine: String,
                                notProcessableReasonType: String,
                                notProcessableReasonMessage: String,
                                stackTrace: Option[String] = None)

// TODO Refacotr sig of validateAndConvertTypes, so that have following as In / Out respectively
case class ParsedRecord(map: Map[String, Any]) extends AnyVal

case class TypedTree(tree: Map[String, Any]) extends AnyVal

object ReadValidated {
  val leftAny: NotProcessableRecordTyped => Left[NotProcessableRecordTyped, Any] = Left[NotProcessableRecordTyped, Any]
  val rightAny: Any => Right[NotProcessableRecordTyped, Any] = Right[NotProcessableRecordTyped, Any]

  implicit class PimpedNotProcessableRecordTyped(r: NotProcessableRecordTyped) {
    def toNotProcessableRecord: NotProcessableRecord = NotProcessableRecord(
      recordLine = r.recordLine,
      notProcessableReasonType = r.notProcessableReasonType.toString,
      notProcessableReasonMessage = r.notProcessableReasonMessage,
      stackTrace = r.stackTrace
    )
  }

  def attemptProcessing[T <: Product : TypeTag : ClassTag : Decoder](sparkSession: SparkSession,
                                                                     path: String,
                                                                     contract: Contract): RDD[Either[NotProcessableRecord, T]] = {
    sparkSession.sparkContext.textFile(path)
    .mapPartitions(parsePartitionToFieldValueMaps(_, contract))
    .map {
      case Right((line: String, fieldsToValues)) =>
        implicit val l = line
        validateAndConvertTypes(
          parsedRecord = fieldsToValues,
          expectedSchema =
            contract.keyConverter.map(_.outToIn).map(KeyConverter.convertKeys(structFor[T], _)).getOrElse(structFor[T]),
          contract = contract
        ) match {
          case Left(fail) => Left[NotProcessableRecord, T](fail.toNotProcessableRecord)
          case Right(map) => Right[NotProcessableRecord, T](
            CirceJsonUtils.mapToCaseClass[T](map, contract.keyConverter.map(_.inToOut))
          )
        }
      case Left(parseFail) => Left[NotProcessableRecord, T](parseFail.toNotProcessableRecord)
    }
  }
  
  def attemptProcessingAsMap(sparkSession: SparkSession,
                             path: String,
                             contract: Contract,
                             schema: StructType): RDD[Either[NotProcessableRecord, Map[String, Any]]] = {
    sparkSession.sparkContext.textFile(path)
    .mapPartitions(parsePartitionToFieldValueMaps(_, contract))
    .map {
      case Right((line: String, fieldsToValues)) =>
        implicit val l = line
        validateAndConvertTypes(fieldsToValues, schema, contract) match {
          case Left(fail) => Left[NotProcessableRecord, Map[String, Any]](fail.toNotProcessableRecord)
          case Right(map) => Right[NotProcessableRecord, Map[String, Any]](map)
        }
      case Left(parseFail) => Left[NotProcessableRecord, Map[String, Any]](parseFail.toNotProcessableRecord)
    }
  }


  def apply[T <: Product : TypeTag : ClassTag : Decoder](session: SparkSession,
                                                         path: String,
                                                         contract: Contract): (Dataset[T], Dataset[NotProcessableRecord]) = {
    val processingAttempted: RDD[Either[NotProcessableRecord, T]] = attemptProcessing[T](session, path, contract)

    (session.createDataset(processingAttempted.flatMap(_.right.toOption))(Encoders.product[T]),
      session.createDataset(processingAttempted.flatMap(_.left.toOption))(Encoders.product[NotProcessableRecord]))
  }

  // Will return None for nulls/missing fields if and only if the field is nullable
  // This will never return an actual null in the map since nullability is determined by Optionality
  // Technically we don't need to use StructType, we could introduce our own recursive container for the structure
  // TODO Full path to field in error message
  def validateAndConvertTypes(parsedRecord: Map[String, Any],
                              expectedSchema: StructType,
                              contract: Contract,
                              fieldPrefix: String = "")
                             (implicit line: String): Either[NotProcessableRecordTyped, Map[String, Any]] = {
    val validatedFields: Map[String, Either[NotProcessableRecordTyped, Any]] =
      expectedSchema.fields.map {
        case StructField(name, StringType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case correctType@(Right(None) | Right(Some(_: String))) if nullable => correctType
            case correctType@Right(_: String) if !nullable => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected String but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, BooleanType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case correctType@(Right(None) | Right(Some(_: Boolean))) if nullable => correctType
            case correctType@Right(_: Boolean) => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Boolean but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, TimestampType, nullable, _) =>
          require(contract.dateFormat.isDefined)

          def parseTimeStampString(field: String): Either[NotProcessableRecordTyped, Any] = try {
            if (nullable) rightAny(Some(DateTimeStringUtils.stringToTimestamp(field, contract.dateFormat.get)))
            else rightAny(DateTimeStringUtils.stringToTimestamp(field, contract.dateFormat.get))
          } catch {
            case e: Throwable =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = InvalidTimestamp,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Could not parse timestamp see stack trace: " + field,
                stackTrace = Some(StackTraceToString(e))
              ))
          }

          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case Right(field: String) if !nullable => parseTimeStampString(field)
            case Right(Some(field: String)) if nullable => parseTimeStampString(field)
            case correctType@Right(None) if nullable => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Timestamp String but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, IntegerType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case correctType@(Right(None) | Right(Some(_: Int))) if nullable => correctType
            case correctType@Right(_: Int) => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Int but found field: " + wrongTypeField,
                //                  + ", class: " + wrongTypeField.asInstanceOf[Option[_]].get.getClass,
                stackTrace = None
              ))
          }

        case StructField(name, LongType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case correctType@(Right(None) | Right(Some(_: Long))) if nullable => correctType
            case correctType@Right(_: Long) => correctType

            // TODO Need tests for these two cases
            case correctType@Right(None) if nullable => correctType
            case Right(Some(i: Int)) if nullable => rightAny(Some(i.toLong))
            case Right(i: Int) => rightAny(i.toLong)


            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Long but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, DoubleType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case correctType@(Right(None) | Right(Some(_: Double))) if nullable => correctType
            case correctType@Right(_: Double) => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Double but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, ArrayType(dataType, containsNull), nullable, _) =>
          // For some reason even though the default for ArrayType is true, using schemaFor it gives false
          val containsNull = true

          def recurse(array: Vector[Any]): Either[NotProcessableRecordTyped, Any] = {
            val indexed: Map[String, Any] = array.zipWithIndex.toMap.mapValues(_.toString).map(_.swap)

            validateAndConvertTypes(indexed, StructType(indexed.map {
              case (index, _) => StructField(index, dataType, containsNull)
            }.toSeq), contract, fieldPath(fieldPrefix, name)) match {
              case Right(map: Map[String, Any]) =>
                rightAny(map.map(_.swap).mapValues(_.toInt).toVector.sortBy(_._2).map {
                  case (Some(thing), _) => thing
                  case (None, _) => null
                })
              case Left(fail) => leftAny(fail)
            }
          }

          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case Right(array: Vector[_]) if containsNull && !nullable => recurse(array)
            case Right(Some(array: Vector[_])) if containsNull && nullable => recurse(array) match {
              case Right(map) => rightAny(Some(map))
              case Left(fail) => leftAny(fail)
            }
            case correctType@Right(None) if containsNull && nullable => correctType
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage =
                  s"Field ${fieldPath(fieldPrefix, name)}. Expected Array but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }

        case StructField(name, structType: StructType, nullable, _) =>
          getField(name, parsedRecord, nullable) match {
            case nullFail: Left[NotProcessableRecordTyped, Any] => nullFail
            case Right(struct: Map[_, _]) if struct.keySet.forall(_.isInstanceOf[String]) && struct.nonEmpty && !nullable =>
              validateAndConvertTypes(struct.asInstanceOf[Map[String, Any]], structType, contract, fieldPath(fieldPrefix, name))
            case Right(Some(struct: Map[_, _])) if struct.keySet.forall(_.isInstanceOf[String]) && struct.nonEmpty && nullable =>
              validateAndConvertTypes(struct.asInstanceOf[Map[String, Any]], structType, contract, fieldPath(fieldPrefix, name)) match {
                case Left(fail) => leftAny(fail)
                case Right(map) => rightAny(Some(map))
              }
            case Right(None) if nullable => rightAny(None)
            case Right(wrongTypeField) =>
              leftAny(NotProcessableRecordTyped(
                recordLine = line,
                notProcessableReasonType = IncorrectType,
                notProcessableReasonMessage = s"Field ${fieldPath(fieldPrefix, name)}. Expected Struct but found field: " + wrongTypeField,
                stackTrace = None
              ))
          }
      }
      .zip(expectedSchema.fields.map(
        field => contract.keyConverter.map(_.inToOut).map(_ (field.name)).getOrElse(field.name)))
      .map(_.swap)
      .toMap

    validatedFields.find(_._2.isLeft).map(_._2.left.get).map(Left[NotProcessableRecordTyped, Map[String, Any]])
    .getOrElse(Right[NotProcessableRecordTyped, Map[String, Any]](validatedFields.mapValues(_.right.get)))
  }

  def fieldPath(fieldPrefix: String, name: String): String =
    if (fieldPrefix == "") name else fieldPrefix + "." + name

  def getField(name: String,
               fieldsToValues: Map[String, Any],
               nullable: Boolean)
              (implicit line: String): Either[NotProcessableRecordTyped, Any] = {
    val valueOption: Option[Any] = fieldsToValues.get(name).flatMap(Option(_))

    if (nullable)
      Right[NotProcessableRecordTyped, Any](valueOption)
    else
      valueOption.toRight[NotProcessableRecordTyped](NotProcessableRecordTyped(
        recordLine = line,
        notProcessableReasonType = MissingField,
        notProcessableReasonMessage = "Missing non nullable field: " + name,
        None
      ))
  }

  // Using Circe because
  // (a) it returns Either,
  // (b) the error message is concise, so can be included in the notProcessableReasonMessage
  // (c) intend to use it instead of spray
  // TODO We should replace all other occurances of spray with Circe
  def parsePartitionToFieldValueMaps(partition: Iterator[String],
                                     contract: Contract): Iterator[Either[NotProcessableRecordTyped, (String, Map[String, Any])]] = {
    contract match {
      case JSON(_, _) =>
        partition.map(line => parse(line) match {
          case Right(json) =>
            Right[NotProcessableRecordTyped, (String, Map[String, Any])](line -> CirceJsonUtils.jsonToMap(json))
          case Left(parsingFailure) => Left[NotProcessableRecordTyped, (String, Map[String, Any])](NotProcessableRecordTyped(
            recordLine = line,
            notProcessableReasonType = InvalidJSON,
            notProcessableReasonMessage = "Could not parse json: " + parsingFailure.message,
            stackTrace = Some(StackTraceToString(parsingFailure.underlying))
          ))
        })

      case CSV(hasHeader, quotedStrings, _, _, _) =>
        // Should use apache commons CSV parser

        ???
    }
  }
}
