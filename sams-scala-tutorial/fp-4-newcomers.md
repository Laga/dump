# Intro

We will try to answer five questions:

 - What is functional programming?
 - Why would we use it?
 - Why Scala in particular?
 - Why is it appropriate in terms of Big Data?
 - Why is it appropriate for server-side web development?

The hope is to mix between bullet points and code snippets that you could copy and paste into a shell then experiement further.  This talk will **not** cover stuff like Monoids, Monads, Functors, Categories, etc.

# What is Functional Programming?

## Things are Functions

You can *apply* most things in Scala as if they were a function, e.g.

```
scala> val mySet = Set(1, 3, 5, 6)
mySet: scala.collection.immutable.Set[Int] = Set(1, 3, 5, 6)

scala> mySet(4)
res0: Boolean = false

scala> mySet(5)
res2: Boolean = true
```

So `mySet` is a function from `Int` to `Boolean`, can you guess what it is?

## Functions are Things

We can declare and pass functions around like things. E.g.

```
scala> val addOne = (i: Int) => i + 1
addOne: Int => Int = <function1>

scala> val list = List(1, 2, 2, 5, 5, 6)
list: List[Int] = List(1, 2, 2, 5, 5, 6)

scala> list.map(addOne)
res6: List[Int] = List(2, 3, 3, 6, 6, 7)
```

## Functions are *really Functions*! AKA Pure Functions

 - That is they are functions in the formal *mathematical* sense
 - They **only** take some parameters and return a result

I.e.

1. They do NOT *change anything*
2. They can NOT depend on *change*

(NOTE: Style and practice - not forced by the language.)

### Breaks 1

```
scala> var iCanChange = 0

scala> def notReallyAFunction(bar: Int): Int = {
     | iCanChange = iCanChange + 10
     | bar
     | }
     
scala> notReallyAFunction(5)
res16: Int = 5

scala> iCanChange
res17: Int = 10
```

This is called a "side effect"

### Breaks 2

```
scala> var iCanChange = 0
iCanChange: Int = 0

scala> def notReallyAFunction(bar: Int): Int = iCanChange + bar
notReallyAFunction: (bar: Int)Int

scala> notReallyAFunction(5)
res9: Int = 5

scala> iCanChange = iCanChange + 3
iCanChange: Int = 3

scala> notReallyAFunction(5)
res10: Int = 8
```

### Pure Function Summary

Basically if you never use variables (in Scala `var`), and never use any code that uses a `var` (e.g. mutable data structures) then your functions will be "Pure" (ignorning weird stuff like IO).

### Key Design Differences to Procedural

 - Passing functions around as things
 - Purity

### Key Design Difference to OOP

 - In OOP you have classes that mix functions, state and data
 - In OOP deduplication and abstraction is acheived using complex class heiarchies, injection and interfaces
 - In FP you keep functions separate to data
 - In FP deduplication is acheived using static functions and ad-hoc polymorphism / type-classes
 - In FP state is delayed, or kept entirely outside the application code (like a DB, filesystem, client state)

So it's much harder to leap from OOP to FP, than Procedural to FP. The switch from OOP requires unlearning, which is harder than learning.

# Why would we use it?

## VS both procedural & OOP

 - Higher Ordered Functions (like map) reduce endless repitition of idioms (like `for` loops)
 - Very easy to reason about as their are no hidden dependencies, nor effects. Little human memory required!
 - Very easy to change FP code. E.g. no matter where you put a line of code, it will always do the same thing!

## VS OOP

 - Principles and patterns become pointless in FP
