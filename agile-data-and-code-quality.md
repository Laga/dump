## Agile Data - Code Structure & Quality

The main cause of bad code and bad code structure in Data Science is **the tight coupling of Analytics with productionization.**

This is a problem born out of the very definition of a Data Scientist; someone in between an analyst, a mathematician and a software developer / engineer.  The analogy in web development is the full stack developer, who may have started out in their career by tightly coupling their view code (the FE) with the business logic (the BE).  The reaction was to invent and formalize a system of decoupling the roles so that systems became cleaner and easier to change, and so came MVC and it's derivatives.  This post will propose a similar structure for Data Science.

A big benifit of good code quality and structure that will be explored in another post will be it's impact on collaboration.  Clean code and structure feeds back into collaborative teams, which itself has many benifits.

### Introducing A2EM

A2EM is an acroynm, mnemonic, "algebriac expression" and represents work flow and separation / decoupling.

**A** - Ad hoc Analysis

**2 x E** - ETL and Evaluation

**2 x M** - Model and Mathematics

We flow from Ad Hoc Analysis 2 ETLs, Evaluation, Models and Mathematics.

The "2" also represents a *separation* between the ad hoc world of interactivity and visualizations and the actual product which is the combination of ETL, models and evaluation.  This separation ought to be physically compounded by splitting a Data Science project out into two repositories and using a different toolset for each. Just as in web development the back end development is done in a different IDE to the front end development. The mantra of MVC is "don't put business logic in your view code" the mantra of A2EM will be "don't put production code, especially ETL & Evaluation, in your Ad hoc interactive environment".

In order for Data Science to embrace the lessons Agile and XP taught to software development, like clean code & design, TDD and cross-funtional teams, we must first embrace a framework that is conducive to clean decoupling of responsibilities and roles.

(If you can think of a neater, easier to remember acronm, please leave your idea on the comments.)

### Step by Step Work Flow of A2EM

#### 1. Create 2 repositories

We highly recommend creating a separate repository for your notebooks and ad-hoc code.  We did try creating a separate directory inside a single repository but this had many problems we detail further below.  The main issue is that notebooks where not designed to be version controlled, they are by definition ad-hoc throw away environments - best used to play and sometimes to present, not to productionize.  So suppose your project is called "my-project", create two repositories, one called "my-project" and one called "my-project-ad-hoc". In the latter you can put your notesbooks (like iScala, iSpark, Jupyter, R-Studio files, etc), interactive environments, scrap code, etc. In the former, henceforth "main" or "EM", your going to put high quality, neatly structured, automatically tested, production worthy code, which you should use a proper IDE for. E.g. if you do Data Science in Scala choose something like Intellij, or if it's Python, choose PyCharm.

Drawbacks of using 1 repository.

(1). Special commit hooks required to attempt to turn notebooks into code for version control, since they are saved into JSON.  This never quite worked; we managed to minimize the JSON down to something smaller but a) it was still JSON, b) git started behaving unpreditably.
(2). Doesn't enforce strict separation that could result in the flow being violated and Data Scientists going back to the old ways of putting all their code in an ad hoc environment. Just as shells are treated as throw away environments, so should notebooks.
(3). More effort to setup, especially if not scripted in some way.
(4). Extra care must be taken to ensure ad-hoc code is not accidentally deployed along with main code.

By keeping the ad-hoc repository completely separate, one can be less strict about committing binary, images or data.  Nevertheless care should be taken to keep the repo size small and use scripts to generate or pull non-code objects as required.

#### 2. Ticket Tracker and Git Flow

Use a light weight tracker like Jira, Trello or even Mingle, avoid monolithic dinasours like ServiceNow.  Then follow the [git flow branching model](http://danielkummer.github.io/git-flow-cheatsheet/) for your "main" repo.  What branching model you use for the ad-hoc repo is up to you as it's not important. I'm aware of teams that even choose to keep notebooks for each team member separate since notebooks are notorious for causing merge conflicts.

This post will not labour over the details of various work flows, rather I have found the following key points are the most important in any Agile work flow:

*(A)* [Done means done](http://www.allaboutagile.com/agile-principle-7-done-means-done/), completely finish a ticket before moving on to the next.  A single 100% done ticket is better than ten 99% complete tickets.
*(B)* Try to map `git flow` commands to the moving of tickets between columns on JIRA.
*(C)* Tickets should only last days.  More than a week and more effort should be made to scope and clarify tasks.

#### 3. Write a Test

Before writting code in the main repo write a test.  Of course you might not know what main code you want to write, so go ahead and use the ad-hoc environment.

It seems to require greater imagination to devise tests for Data Science and Big Data Engineering when compared to "traditional software development".  Rather than labour over the details here, I will dedicate an entire post to applying TDD principles to Data Science & Engineering. For now I'll just say that as experiments are key to Science, automatic tests are key to Data Science.

#### 4. Ticket Completion - Tidy, Review and Run

**A ticket should not be considered completed if no code has been committed to the main repo.** This can be some little utility functions you wrote to help you with your ad hoc analysis, a script to generate some images / html, some ETL code, some evaluation code, some mathematical functions or some modelling code.  You should then remove that code from the ad hoc repo and import it as a library from the main code upon future use.  You should not end up with production code in your ad hoc environment, and you definitely should not end up with any hacky ad hoc cruft in your main repo.

Ensure your code is clean and tidy and submit it for review to a colleague.  The colleague ought to be able to run scripts and tests in your main repo and see what you have acheived.  The colleague should not have to open your notebooks, or whatever ad hoc environment you use, in order to see what you have been doing.  The colleague should consider the ticket complete if and only if what they observe you have done is exactly what the ticket stated, the code is clean, and the code is automatically tested.

#### 5. Ship It

**Artefacts for deployment onto production environments should only be from the main repo.**  Be sure to use an artefact repository and CI along with git flow. You should be releasing once a week or fortnight. It might take many sprints to get to a full ETL -> Model -> Evaluation pipeline but **get into the habbit of releasing even when it's not perfect**. Downstream processes for taking the data and using it need not hook in until you are happy the data is of suitable quality, nevertheless you should be showing business value and progress every sprint. Agile embraces the philosophy that nothing is ever truly complete, most things are an ongoing iterative process. The smaller the iterations the faster the ultimate aim will be met and the higher the quality will be.

### Summary

The A2EM code structure and work flow model is designed to maximise Agile principles. Feedback loops should be many, and should be as tight as possible. Automatic tests allow for constant code iteration and instant technical feedback. Well structured code reviews on clearly defined objectives provide immediate feedback as to whether the objectives have been met. Writing libraries with tests and entry points with integrations tests, even when doing exploratory work, means your work is clear, repeatable, scientific and easily transferable to a colleague. Frequent releases and interaction with stakeholders provide feedback on the wider picture and ensure the project cannot deviate too far from what stakeholders have in mind. These feedback loops allow for greater exploration and faster changes in direction while preserving quality. "You don't go fast by rushing" in one direction, with one tool, with one person, with one way of thinking. You go fast by being Agile.
