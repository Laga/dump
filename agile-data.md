## Agile Data Scientists Do Scale

Subtitle: Time to deliver!

Thanks to the hype and rapid growth of Big Data Engineering and Data Science it seems most companies and practioners have got so excited by hiring, building infrastructure, clever sounding words and shiny technology that one crucial part of the fields seems to be missing - **delivery**.  I hear of countless stories where teams are built, clusters are bought, prototype algorithms written and software is installed, but it then takes months or even longer to deliver working data driven applications.  It happens in startups and big corps alike, hype is thick in the air but delivery is thin on the ground.

I felt it necessary to write this and two follow up posts after reading a Harvard Business Review "Data Scientist's Don't Scale" and related blogs 

https://hbr.org/2015/05/data-scientists-dont-scale
http://blogs.wsj.com/cio/2014/04/30/its-already-time-to-kill-the-data-scientist-title/
http://www.zdnet.com/article/data-scientists-dont-scale/

A decade or two ago delivery used be a problem in the software development industry as a whole.  Releasing working software would also take months and even years.  Then came along Agile, XP, Scrum and TDD and for those correctly practicing it it seemed to solve the problem and suddenly working software was delivered every few weeks.

I have written two posts on how to apply Agile methodlogies in Data Science and Big Data. The two areas I have written about were chosen because they are critical in correctly practicing Agile while being the most overlooked and misunderstood practices in the data proffesions.

Automatic tests are absalutely critical in correctly practicing Agile [1], [2], and from TDD evolved more acronyms and terms than many Data Scientists have written tests; there is TDD, BDD, DDD, ATDD, SDD, EDD, unit tests, integrations tests, black box tests, end-to-end tests, systems tests, acceptence tests, property based tests, example based tests, etc. So I wrote: (LINK)

The second most lacking part in the data proffesions seems to be a lack of high-quality code and cross-functional teams.  Most definitions of Data Science actually include "hacking" as a skill.  If writting crap code is part of the definition of Data Science it's no wonder then that the proffesion is failing to deliver.  Cross-functional teams and cross-functional team members have the obvious benifit of being able to deliver end to end working (data-driven) software.  In the data proffesions this means the team must be able to ETL, clean, prototype, collaborate **and productionize**.  Collaboration and productionization cannot happpen without high-quality code, which is why I consider the two principles to go hand in hand. So I wrote: (LINK)

### Why Should Data Proffesionals Care?

Data Scientists and Big Data Engineers have a collective moral responsibility to be Agile and focus more on delivery and less on having fun with the latest tech/algorithms.  When executives start noticing lack of return on investment the consequences will be awful, not only for the proffesion but for the business, and comes in two forms.

**Magic SaaS solutions**:  There may exist a genuinely powerful magic SaaS solution that automates away the job of data proffesionals. Nevertheless it's just common sense that in order to automate a very complicated field one would need a vastly more complicated application.

**Outsourcing solutions**: There may also exist some genuinely efficient companies to outsource to.  Nevertheless it's just common sense that paying X data proffesionals to deliver a project ought to be cheaper than paying X data proffesionals plus managers, marketers, salespersons and shareholders, etc to do the same thing but in a domain they have not even seen.  Such a company must be vastly more efficient than ones own.

Eventually such solutions may emerge, but not this decade.

The problem is that many managers/executives, particularly those that have not slowly evolved into the role from scientific or engineering careers, will be easily duped by colourful slides from salepersons.  They understand colours and charm, but they don't understand 90% of what comes out of our mouth and don't like our spikey personalities.

**But** there is hope.  It has happened in many companies since the early 2000s in software development - teams became Agile. Consequently projects got delivered and executives got their glory and bonuses, which is something else they can comprehend.  Agile in-house teams would often be chosen over the solutions providers, consequently the businesses saved money, developers made money, and society overall become more efficient.

This is why you should care.

## Agile Data and *DD

This post will not labour over the subtle differences between the annoying number of terms mentioned in the precursor post (LINK), which will henceforth be collectively referred to as *DD, rather we will focus on how to apply the principles they each have in common to a field that at a glance precludes using them.  *DD has worked well in web development but it seems difficult to apply in data driven fields.

### "Problems" of *DD in Data Science and Big Data

#### Data Science

*Evaluation*: How does one write an "automatic test" for a model? What is the definition of passing? We usually look at the ROCs, some charts and evaluation measures and consider the model "good" when the numbers are large (but not so large that something fishy is going on with our features or validation framework).

*Model Exploration*: Often using single measures of performance doesn't really make sense, rather we look at charts, we add more colours, we look at clusters, and when it seems kinda reasonable we stop and consider the task done.

*Data Exploration*: We also have a habit of postponing automation, of say ETL, or running the models, or evaluating the models.  That isn't prototyping, that's productionization, and we will save that for later or get someone else to do it. We say to ourselves "we don't have an awesome model yet, why would I put any effort into productionization?".

The process is interactive and iterative, which is good, but it is also quite manual, which is bad.

#### Big Data

*Long Running Jobs*: How can a test-cycle work for something that takes 5 hours to run? Aren't tests supposed to run in a few seconds?

*Computational Resource Problems*: How can we write a test to catch out of memory errors? Or out of disk space errors?

### The Core Principle of *DD Practices

The "problems" we use to justify not writting automated tests in the data world are not problems with the practice, but the mindset.  Lack of automation in *any* type of software development is driven by:

 - Fear of delivering something that isn't perfect and awesome
 - lack of imagination on how to test
 - lack of clarity of objectives
 - a natural propensity to focus on the fun and clever stuff, not on delivery

Now communicating the core *DD principle can solve this problem of mindset, but over the years the core has been buried under superfluous aspects, such as frameworks, tools, ubiquitis languages, speed, and over emphasis on writting test code before writting main code.

So *the core principle of *DD practices* is just:

1. Defining the use case in such simple and unambiguous terms so that success can be defined formally and even executed by a machine
2. Doing 1. *before* trying to solve the problem



### How to Apply *DD

[1] - https://www.youtube.com/watch?v=hG4LH6P8Syk
[2] - http://en.wikipedia.org/wiki/Agile_software_development#Agile_practices
