
## Agile Data, Code Quality and Cross Functional Teams

In this post we will focus on how delivery velocity can be increased and *maintained* via the below Agile and XP principles adapted to Data Science

 - Teams that can build every part of the system, including even infrastructure and deployment
 - Code, design, (mathematics) and architecture is kept clean so that at any given point in time adding to the system is easy

Neither of these things currently are accepted and respected practices in the Data Science world at the moment.  The cause of this is due to a single problem:

Analytics has become tightly coupled with productionization.

This is a problem born out of the very definition of a Data Scientist; someone in between an analyst / mathematician and a software developer / engineer.  The analogy in web development is the full stack developer, who may have started out in their career by tightly coupling their view code (the FE) with the business logic (the BE).  The reaction was to invent and formalize a system of decoupling the roles so that systems became cleaner and easier to change, and so came MVC and it's derivatives.  The first section of this post will propose a similar structure for Data Science.

Data Science has been defined as "a person who is better at maths than a coder and better at coding than a mathematician", but the converse of this is less appealing - "someone who is a worse coder than developer and worse at maths than a mathematician". In the second section of this post we will discuss how to get your Data Scientists to go from jack of two trades to a master of both with cross functional teams.  Cross functional teams then feed back into cleanliness, and cleanliness feeds back into closer collaboration. Get the two right and you have yourself a positive feedback loop that will pump out products at speed.

### Introducing A2EM

A2EM is an acroynm, mnemonic, "algebriac expression" and represents work flow and separation / decoupling.

**A** - Ad hoc Analysis

**2 x E** - ETL and Evaluation

**2 x M** - Model and Mathematics

We flow from Ad Hoc Analysis 2 ETLs, Evaluation, Models and Mathematics.

The "2" also represents a *separation* between the ad hoc world of interactivity and visualizations and the actual product which is the combination of ETL, models and evaluation.  This separation ought to be physically compounded by splitting a Data Science project out into two repositories and using a different toolset for each. Just as in web development the back end development is done in a different IDE to the front end development. The mantra of MVC is "don't put business logic in your view code" the mantra of A2EM will be "don't put production code, especially ETL & Evaluation, in your Ad hoc interactive environment".

In order for Data Science to embrace the lessons Agile and XP taught to software development, like clean code & design, TDD and cross-funtional teams, we must first embrace a framework that is conducive to clean decoupling of responsibilities and roles.

### Step by Step Work Flow of A2EM

#### 1. Create 2 Repositories

Suppose your project is called "my-project", create two repositories, one called "my-project" and one called "my-project-ad-hoc".  In the latter you can put your notesbooks (like iScala, iSpark, Jupyter, R-Studio files, etc), interactive environments, images, etc.  In the former, henceforth "main" or "EM", your going to put high quality, neatly structured, automatically tested, production worthy code, which you should use a proper IDE for. E.g. if you do Data Science in Scala choose something like Intellij, or if it's Python, choose PyCharm.

You need 2 repositories because unfortunately most notebooks save as data, not code, which completely breaks history.  Still try to keep your images and actual data untracked as these things might bloat the size.  Instead ensure scripts can generate the images or grab the data from a warehouse as required.

Note: I like using no suffix/prefix for the main repo as it is primary, only the ad hoc repo deserves a suffix/prefix as it should be considered secondary to the main repo.

#### 2. Ticket Tracker and Git Flow

Use a light weight tracker like Jira, Trello or even Mingle, avoid monolithic dinasours like ServiceNow.  Then follow the [git flow branching model](http://danielkummer.github.io/git-flow-cheatsheet/) for your "main" repo.  What branching model you use for the ad-hoc repo is up to you as it's not so important.

This post will not labour over the details of various work flows, rather I have found the following key points are the most important in any Agile work flow:

*(A)* [Done means done](http://www.allaboutagile.com/agile-principle-7-done-means-done/), completely finish a ticket before moving on to the next.  A single 100% done ticket is better than ten 99% complete tickets.
*(B)* Try to map `git flow` commands to the moving a ticket between columns on JIRA.
*(C)* Tickets should only last days, not a week or more.  If they take too long it means you have incorrectly scoped.

#### 3. Write a Test

Before writting code in the main repo write a test according to [Agile Data and *DD](LINK). Of course you might not know what main code you want to write, so go ahead and use the ad-hoc repo.

#### 4. Ticket Completion - Tidy, Review and Run

A ticket should not be considered completed if no code has been committed to the main repo. This can be some little utility functions you wrote to help you with your ad hoc analysis, a script to generate some images / html, some ETL code, some evaluation code, some mathematical functions or some modelling code.  You should then remove that code from the ad hoc repo and import it as a library upon further use.  You should not end up with production code in your ad hoc environment, and you definitely should not end up with any hacky ad hoc cruft in your main repo.

Ensure your code is clean and tidy and submit it for review to a colleague.  The colleague ought to be able to run scripts and or tests in your main repo and see what you have acheived.  The colleague should not have to open your notebooks, or whatever ad hoc environment you use, in order to see what you have been doing.  The colleague should consider the ticket complete if and only if what they observe you have done is exactly what the ticket stated, the code is clean, and the code is automatically tested.

#### 5. Ship It

Artefacts for deployment onto production environments should only be from the main repo.  Be sure to use an artefact repository and CI along with git flow. You should be releasing once a week or fortnight. It might take many sprints to get to a full ETL -> Model -> Evaluation pipeline but get into the habbit of releasing even when it's not perfect. Downstream processes for taking the data and using it need to hook in until you are happy the data is of suitable quality, nevertheless you should be showing business value and progress every sprint. Agile embraces the philosophy that nothing is ever truly complete, most things are an ongoing iterative process. The smaller the iterations the faster the ultimate aim will be met and the higher the quality will be.

### Cross Functional Data Science Teams

Cross functional teams are important in Data Science firstly because it's a known fact that multiple specialized teams deliver much slower than a single cross functional team.  Returning to the web development analogy: having a back-end team, a front-end team, a team of DBAs, a team of system administrators, and a team of designers, may seem logical to a factory manager from the industrial revolution, but in software development it's completely counter productive.  Rather a single team devoted to a handful of projects with all the skills necessary to deliver those projects has been shown to be orders of magnitude faster.

Secondly I have heard and seen increasing frustration in the market as to what actually is a "Data Scientist", and how finding candidates with the full skill set seems to be extremly hard.  How can a company build "a team of half a dozen Data Scientists", when they cannot even seem to find one.  When the goal is unrealistic, the posts should shift, and here the focus should be on building a "Data Science Team", not "a team of Data Scientists".  To do that we need experts in many different fields that then mentor each other. By using Agile practices like paring and code review, hopefully over time team members asymptotically approach the perfect "Data Scientist" while constantly delivering high quality Data Science products.

Some people have observed that Data Scientists spend a lot of time "cleaning data", the reaction for some is to then hire more people whose sole job it is to clean data.  Not only is this creating a very boring job but it's treating the symptoms not curing the cause.  Rather than digress too much in this post, please see [Dirty Data is a Process Problem](LINK).

The key roles you do need in a Data Science Team, which may or may not be shared by a single person are:

1. **Software Developer**: Role is to review code, ensure high quality code, ensure *DD is followed, and ensure proper work flow process is followed.
2. **Machine Learning Expert**: This role is most similar to what we call "Data Scientists", they must deeply understand the process of training, prediction and evaluation, and effective modelling techniques. They may be most used to scripting languages like R and Python, but must be willing to learn "production" languages like Scala and Java.
3. **Mathematician**: This role is emphasized distinctly from the above in that they ought to have a mathematics degree.  Mathematics, especially probability, has many subtleties and depth that mean misuse is commonplace through having only a superficial understanding.  The Mathematician must keep a watchful eye on evaluation techniques and model details.
4. **Big Data Engineer**: Role is to contribute skills regarding complexity theory, writting Map Reduce jobs, understand Big Data Databases, oversea ETL, ensure high quality engineering and ensure fast efficient pipelines.
5. Half of a **Big Data DevOps**: This role is the most understated yet important role in the entire Big Data era.  The logic is simple, no infrastructure, no Data Science.  This skills required to be a good Big Data DevOps are also the most understated - good DevOps can save you millions by correctly and efficiently configuring infrastructure. Now a whole DevOps employee per team may be excessive, I'd say as a rule of thumb you need one full DevOps for every eight Data Scientists, or every two teams.

Here importance, and definitely recruitment order, is 5 and 4 first - you need computers and data before you can do Data Science. If you employ some Data Scientists without first having some DevOps and Big Data Engineers in place, quite frankly this is stupid, that would be like hiring some roofers without hiring any brick layers.

#### Pair Programming

#### Code Review

#### Collaboration and Time Management

### A Note On Language and Tooling

I hate debugging and I hate having to run code to find out what it does.  I write code that works first time. If my code compiles and the tests pass, I ship it.  This is because I don't write procedural code, don't mutate state and don't confuse types.  Of course this means I write in a statically typed functional language. Nevertheless always always

**Choose best tool for the job.**

Sometimes I use Python, which I don't like, sometimes I use bash, and sometimes I learn tools I have no desire to learn.  I only use Python when there is some library that doesn't exist in Scala and I'll encapsulate it in a TSV interface. Mostly I choose Scala because Scala is usually the best tool for the job ...

Scala means you can prototype and deploy in a single language.

Statically typed.









### 

A2EM

Gianmario:

ACEM

MEAC

CEAM

CEMA

MECA


IVMECA

IVMECA

I don't like Computation or Driver - it's implementation details, and may not necessary. 

VAMP - Visualization & Analytics. Models, Mathematics & Productization.

(Footnote: Alternatives, AV2EM, AI2EM, IV2EM, V2EM, A2P (p for product), IV2P, )

AWESOME ACRONYM



If the ticket is quite technical/mathematical and just a step in order to achieve some other task that is more clearly business facing, then at least ensure the tickets are linked together using links, labels, epics or whatever - so that a business person could trace the ticket through the tracker and see it's (in)direct business value.





- code quality
- language choice and tool choice.  Particularly static typing.
- pair programming (change every 30 mins, have two keyboards plugged into single computer, ensure shortcuts are the same across team).
- Code review
- Document review
- Atlassian stack is awesome
- Phds are unnecessary, more than one per team is unnecessary
- two projects approach - one for notebooks one for libraries, tests and entry points

Engineers are roughly evaluated by the number of technologies multiplied the number of years of use.
Data Scientists seem to currently be roughly evaluated by the number of out-of-box algorithms they can list multiplied by the number of projects they have applied it to.

So engineers are happy to learn new technology and languages, it gets them excited, "great another acronym for my CV".  Data Scientists seem less enthused and just want to stick with whatever tools they are used to.  On a Software Development CV you will see a lot of tech, but you will also see a lot of focus on Agile methodologies, especially for the more experienced CVs.

In order to increase collaboration, ramp up, cross project collaboration, job satisfaction, focus and productivity the team will try to implement more pairing.  We will also try to ensure each member has at least one JIRA from one project that is not their main project.  Such tickets are good candidates for pairing tasks.  We should aim to rotate and pair at least twice a week since the day to day distractions of BAU often mean we do not initialise pairing, nor do we want to drop what we are doing, I propose we use the Calendar to book slots.  Each slot ought to be either 10 - 12 or 14 - 16/17.  We should put the invites in at sprint planning, of course such slots can be moved, but once created it's harder to overlook.  After doing some reading I consider the following techniques to be ways to do good pairing, each rule is a rule of thumb not a law of god

1. Pairing slots should be put into the calendar.  Fridays should perhaps be avoided as such days often have demos, releases, retros, interviews, long lunchs, early finishes, etc.
2. Driver and navigator should switch every 30 minutes. Try to stick to this, this is quite important. Set a timer.
3. Take a 5 minutes break at switches (if necessary), sometimes it can make sense for the navigator to take a short break while the driver is deep in thought
4. Plug in two keyboards into the machine
5. Shortcuts should not be changed from the defaults. If someone insists on non-standard shortcuts, it's up to them to write two scripts to switch between their config and the default quickly.
6. Pairs should take it in turns between slots as to which desk to sit at (since the configuration of the desk will usually favour the desk owner).
7. Navigator must not have their own computer (or phone) near them
8. The driver should always be communicating, pairing is not one person codes and the other watches.
9. Not is it one person types and the other tells them what to type.  If the configuration is Novice-Expert Driver-Navigator resp, the navigator should try to use language that the driver understands, but not give them so much detail that the driver is just typing for the navigator.
