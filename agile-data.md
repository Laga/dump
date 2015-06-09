## Agile Data Scientists Do Scale

Subtitle: Time to deliver!

Picture: Venn diagram with "Software Development Skills", "Maths, particularly Probability", "Conception of MVP + iteration" title "Agile Data"

Thanks to the hype and rapid growth of Big Data Engineering and Data Science it seems most companies and practioners have got so excited by hiring, building infrastructure, clever sounding words and shiny technology that one crucial part of the fields seems to be missing - **delivery**.  I hear of countless stories where teams are built, clusters are bought, prototype algorithms written and software is installed, but it then takes months or even longer to deliver working data driven applications, or for insights to be acted on.  It happens in startups and big corps alike, hype is thick in the air but delivery is thin on the ground.

The Harvard Business Review [Data Scientist's Don't Scale](https://hbr.org/2015/05/data-scientists-dont-scale) and related blogs correctly point out that we should focus on AI **applications**, that is automation. My addition is that these applications can not be easily bought in for most domains, rather they should be built in-house and the builders ought to be **Agile** Data Scientists and Big Data Engineers. The title of Data Scientist is not dead, but keeping Data Science alive means shifting the focus of the Data Scientist away from hacking, ad hoc analysis and prototyping and on to high quality code, automation, applications and Agile methodologies.

Those Data Scientists that do not adapt into the role of the automator will be automated.

A decade or two ago delivery used be a problem in the software development industry as a whole.  Releasing working software would also take months and even years.  Then came along Agile, XP, Scrum and TDD and for those correctly practicing it it seemed to solve the problem and suddenly working software was delivered every few weeks.

I have written two posts on how to apply Agile methodlogies in Data Science and Big Data. The two areas I have written about were chosen because they are critical in correctly practicing Agile while being the most overlooked and misunderstood practices in the data proffesions.

**Automatic tests** are absalutely critical in correctly practicing Agile [1], [2], and from TDD evolved more acronyms and terms than many Data Scientists have written tests; there is TDD, BDD, DDD, ATDD, SDD, EDD, CDD, unit tests, integrations tests, black box tests, end-to-end tests, systems tests, acceptence tests, property based tests, example based tests, functional tests, etc. So I wrote: (LINK)

The second most lacking part in the data proffesions seems to be a lack of **high-quality code** and **cross-functional teams**.  Most definitions of Data Science actually include "hacking" as a skill.  If writting crap code is part of the definition of Data Science it's no wonder then that the proffesion is failing to deliver.  Cross-functional teams and cross-functional team members have the obvious benifit of being able to deliver end to end working (data-driven) software.  In the data proffesions this means the team must be able to ETL, clean, prototype, collaborate **and productionize**.  Collaboration and productionization cannot happpen without high-quality code, which is why I consider the two principles to go hand in hand. So I wrote: (LINK)

Given sufficient interest I may write a couple more posts on applying Agile principles to archetecutre and even modelling.

### Why Should Data Proffesionals Care?

Data Scientists and Big Data Engineers have a collective moral responsibility to be Agile and focus more on delivery and less on having fun with the latest tech/algorithms.  When executives start noticing lack of return on investment the consequences will be awful, not only for the proffesion but for the business, and comes in two forms.

**Magic proprietary SaaS solutions**:  It's just common sense that in order to automate a complicated field one would need a vastly more complicated application. So usually such solutions don't work, it's only for domains which are common to dozens of businesses that such applications can be built externally and bought in.

**Outsourcing solutions**: There may also exist some genuinely efficient companies to outsource to.  Nevertheless it's just common sense that paying X data proffesionals to deliver a project ought to be cheaper than paying X data proffesionals plus managers, marketers, salespersons and shareholders, etc to do the same thing but in a domain unfamiliar to them.  Even if the employees are being paid peanuts such a company must be vastly more efficient than ones own.

The problem is that many executives, particularly those that have not slowly evolved into the role from scientific or engineering careers, can be easily duped by claims of silver bullets from salepersons.  The claim is usually "Pay X get Y and if not, sue us", which is easy to comprehend, but the reality is usually "Pay X get a fraction of Y and don't sue them anyway".

**But** there is hope.  It has happened in many companies since the early 2000s in software development - teams became Agile. Consequently projects got delivered and executives got their glory and bonuses, which is something else that's easy to comprehend.  Agile in-house teams would often be chosen over the solutions providers, consequently the businesses saved money, developers made money, and society overall became more efficient.

This is why you should care.  It's also up to you to change the culture bottom up because Agile happens mainly at the bottom.  All the executives can do is point and shout "hey do Agile, I don't know what it is, but do it!", so it's up to you to read and watch videos so you can know what it is.  Then applying the same principles to data may be challanging, but there are analogies that link the process of web development to the process of data science/engineering.

My Posts:

LINKS again to follow up posts.

Random Links:

[Agile Manifesto](http://agilemanifesto.org/)

[Clean Architecture and Design](https://www.youtube.com/watch?v=Nsjsiz2A9mg)

[Clean Architecture](https://www.youtube.com/watch?v=Nltqi7ODZTM)

[Simple Design](http://www.jamesshore.com/Agile-Book/simple_design.html)

[Done Means Done](http://www.allaboutagile.com/agile-principle-7-done-means-done/)

[(another) Simple Design](http://guide.agilealliance.org/guide/simple-design.html)


## Agile Data and *DD

This post will not labour over the subtle differences between the annoying number of terms mentioned in the precursor post (LINK), which will henceforth be collectively referred to as *DD, rather we will focus on how to apply the principles they each have in common to a field that at a glance precludes using them.  *DD has worked well in web development but it seems difficult to apply in data driven fields.

#### "Problems" of *DD in Data Science

*Evaluation*: How does one write an "automatic test" for a model? What is the definition of passing? We usually look at the ROCs, some charts and evaluation measures and consider the model "good" when the numbers are large (but not so large that something fishy is going on with our features or validation framework).

*Model Exploration*: Often using single measures of performance doesn't really make sense, rather we look at charts, we add more colours and dimensions, we look at clusters, and when it seems kinda reasonable we stop and consider the task done.

*Data Exploration*: We also have a habit of postponing automation, of say ETL, or running the models, or evaluating the models.  That isn't prototyping, that's productionization, and we will save that for later or get someone else to do it. We say to ourselves "we don't have an awesome model yet, why would I put any effort into productionization?".

The process is interactive and iterative, which is good, but it is also quite manual, which is bad.

#### "Problems" of *DD in Big Data

*Long Running Jobs*: How can a test-cycle work for something that takes 5 hours to run? Aren't tests supposed to run in a few seconds?

*Computational Resource Problems*: How can we write a test to catch out of memory errors? Or out of disk space errors?

### The Core Principle of *DD Practices

The "problems" we use to justify not writting automated tests in the data world are not problems with the practice, but the mindset.  Lack of automation in *any* type of software development is driven by:

 - Fear of delivering something that isn't perfect and awesome
 - lack of imagination on how to test
 - lack of clarity of objectives
 - a natural propensity to focus on the fun and clever stuff, not on delivery

Now communicating the core *DD principle can solve this problem of mindset, but over the years the core has been buried under superfluous aspects, such as frameworks, tools, ubiquitis languages, speed, and over emphasis on writting test code before writting main code.

So **the core principle of *DD practices** is just:

1. Defining the use case in such simple and unambiguous terms so that success can be defined formally and even executed by a machine
2. Doing 1. *before* trying to solve the problem

Definition of a proffesional:
Not ship a line of code that does not have an executing unit test - https://www.youtube.com/watch?v=KtHQGs3zFAM

Other guy responds with CDD - similar to property based tests.

Definition of legacy code:
Code that does not have tests.

### How to Apply *DD

- Large cluster for dev, down scale for prod
- Decouple your ETL from your model and from your evaluation framework and use TSVs to interface between them.  Then you can use a real language for as much as possible.  By real langue I mean a statically typed language, Java, Scala, C#, Julia, TypeScript, etc, and if you are in dealing with Big Data you will want to use Scala. Unlike in R and Python, when you write some code in a typed language you know what it does.  There are still notebooks for Scala, (LINKS), but you will find you do not need notebooks for much other than visualization - you do not need to run your code to know what it's doing in a statically typed world.  Anyway by using a real language you also get all the powerful testing frameworks, and you will need to write much less tests.  Only use scripting languages for just that, scripting, short one page scripts that call some scikit learn library that hasn't been written in Java or Scala yet.
- Delete code that when removed no tests fail.

- Now principle 1. is a controversial one, particularly actually writting tests before writting main code.  I personally break this rule from time to time because I'm unclear on what the interface/structure of the main code will end up looking like, or if the code will even need a test because I may end up solving the problem with a library/native then third party.  But what I will do is write a clear description of what the test should be in the test classes with a dummy test that will remind me to fill it in later, e.g. 1 must_== 2, this will fail, preventing me from merging into develop (thanks to CI pipelines).  Then I am forced to come back later and fillin the test.  The important thing is to think about what it is you are trying to do in clear unambiguous terms, **before** you try doing it.

### Do not dogmatically follow anything

Notes from confluence on when not to abstract, DHH.

Mapping tickets to use cases and mapping tickets to a test.

## Agile Data, Code Quality and Cross Functional Teams

- code quality
- language choice and tool choice.  Particularly static typing.
- pair programming (change every 30 mins, have two keyboards plugged into single computer, ensure shortcuts are the same across team).
- Code review
- Document review
- Atlassian stack is awesome
- Phds are unnecessary, more than one per team is unnecessary

In order to increase collaboration, ramp up, cross project collaboration, job satisfaction, focus and productivity the team will try to implement more pairing.  We will also try to ensure each member has at least one JIRA from one project that is not their main project.  Such tickets are good candidates for pairing tasks.  We should aim to rotate and pair at least twice a week since the day to day distractions of BAU often mean we do not initialise pairing, nor do we want to drop what we are doing, I propose we use the Calendar to book slots.  Each slot ought to be either 10 - 12 or 14 - 16/17.  We should put the invites in at sprint planning, of course such slots can be moved, but once created it's harder to overlook.  After doing some reading I consider the following techniques to be ways to do good pairing, each rule is a rule of thumb not a law of god

1. Pairing slots should not be too long (like an entire day), with the same person
2. Driver and navigator should switch every 30 minutes. Try to stick to this, this is quite important. Set a timer.
3. Take a 5 minutes break at switches (if necessary), sometimes it can make sense for the navigator to take a short break while the driver is deep in thought
4. Plug in two keyboards into the machine
5. Shortcuts should not be changed from the defaults. If someone insists on non-standard shortcuts, it's up to them to write two scripts to switch between their config and the default quickly.
6. Pairs should take it in turns between slots as to which desk to sit at (since the configuration of the desk will usually favour the desk owner).
7. Navigator must not have their own computer (or phone) near them
8. The driver should always be communicating, pairing is not one person codes and the other watches.
9. Not is it one person types and the other tells them what to type.  If the configuration is Novice-Expert Driver-Navigator resp, the navigator should try to use language that the driver understands, but not give them so much detail that the driver is just typing for the navigator.


[1] - https://www.youtube.com/watch?v=hG4LH6P8Syk
[2] - http://en.wikipedia.org/wiki/Agile_software_development#Agile_practices
