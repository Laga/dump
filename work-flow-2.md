
# Glossary

Task Tracker = An Agile task tracker with a horizontal board, like Trello or JIRA (nearly all other task/ticker trackers suck)

# Fundemental Principles

 - To ensure that an actions and history on the Task Tracker corresponds to an actions and history in git
 - To ensure than a deployment to an environment corresponds to an action in the Task Tracker and git
 - Done means DONE! http://www.allaboutagile.com/agile-principle-7-done-means-done/
 - To ensure rollback is easy
 - To ensure hotfixing is easy
 - To ensure we always know exactly what code is on production
 - To automate as much as possible so that releases are easy, regular and stress free
 - To be as simple as possible, to minimize unnecessary steps, branches, columns, etc

# Work Flow

## Board

The board should have the following columns:

 - **Backlog / Icebox**: A long list of placeholders for work that needs to be done, not necessarily analysed, nor fleshed out
 - **TODO**: A list of tickets that the team has decided need to be done.  Hopefully some details should be fleshed out on the ticket sufficient for development to start.
 - **Doing**: In progress tickets, in progress includes development, testing and code review.
 - **Ready to release**: A ticket that has been developmed, tested and reviewed and is ready to release
 - **Done**: Released tickets

## Feature Process Step by Step with Git and CI

### Backlog -> TODO

How tickets move from the Backlog to TODO is not particularly important from a development point of view, so will not be documented in detail.  This could be via Sprint plannings (Scrum), or via a Kanban approach, whatever, such details are largely irrelevant to the core Agile methodology.

The only requirement is that a ticket has been reduced to the smallest self contained deliverable, that is:

1. If the ticket was released it would be callable, i.e. there exists a way the customer/user can interact with the application that would execute the corresponding code.  If this is not the case, the code is dead code and should not be in production.

2. It is impossible for the ticket to be broken down into N tickets where each satisfies 1.

*(Desirable, but hard)* Automatically send a slack message

### TODO -> Doing

If you already have a ticket in Doing, but for whatever reason that ticket is pending/blocked, you should first slide it back to TODO and ensure comments on the ticket explain why it's been moved back.  Otherwise:

1. Create a branch *from master* that uses the ticket reference number as a prefix to the branch name.  In Trello these are just short numbers, in JIRA these are usually a short string that includes a project ID and a ticket number.  For example we may have a ticket "5 add back button", then the branch would be 5-add-back-button (observe lower kebab case convention).
2. Slide the ticket from TODO to Doing and add a comment with the branch name (desirable). Ensure the ticket is assigned to you.
3. Implement the ticket via ATDD, BDD, TDD cycles (TDD may not be possible for languages that don't naturally support unit tests).
4. While you work on the ticket you should regularly merge master into your branch, and backup your branch by pushing it.  If you don't merge master into you branch regularly merge conflicts may become unweildy. *When you commit use the ticket reference as a prefix to the commit message.*
5. When you think you have finished **ask someone to review your branch** by mentioning them on the ticket.  It's nice to use a tool like Intellij, github or crucible to review code, but not necessary. There may be some back and forth to tidy things up between the reviewer and the reviewee.
6. If they are happy the code is good, they should say "I'm happy, review passed" or something on the ticket, and they should perform step 1 in the next section

*(Desirable, but hard)* Automatically send a slack message

*(Desirable, but hard)* based on the comment, automatically perform the next step:

### Doing -> Ready to Release

1. The aforementioned reviewer should login to Jenkins and there should be a "merge branch" job or similar, with a parameter that is the branch.  The reviewer should run the job.  This job should:
    - Checkout/pull the branch
    - *(Desirable, but hard)* Check that the user is not the assignee of the ticket
    - Run the test suite, this may include a suite that spins up temporary staging clusters
    - If the test suite passes, it merges the feature branch into master and pushes master.
    - *(Desirable, but hard)* automatically do step 2
2. Move the ticket to "Ready to Release"

*(Desirable, but hard)* Automatically send a slack message

### Ready to Release -> Done/Released

*(Desirable, but has caveats)* A merge to master should automatically trigger this step, but this does mean a feature branch should only be completed if it is to be deployed.

This may result in the releasing of other features.

1. In Jenkins click the "release" button (or "deploy") for the project, now all the following steps ought to be performed by Jenkins automatically:
2. checkout/pull master
3. build the artefact (which will include running the unit tests). Note for some projects that are script based "building" might just be trivial
4. Run the full test suite using that artefact (including extra tests that may be slow but are important before a release)
5. If the tests pass tag the commit (by bumping the **major** version number of the last tag) and push the tag. *(Desirable)* If they fail, send round an email of shame.
6. Put the artefact into the artefact repo (e.g. nexus, s3, etc, something that doesn't allow overwrites)
7. Then if necessary (i.e. for permanent clusters) trigger a deploy job that deploys the artefact to the prod environment

*(Desirable, but hard)* Automatically send a slack message

*(Desirable, but hard)* Automatically move all the tickets that got deployed to Done.

*(Desirable, but hard)* Automatically create release notes and commit them to a .md file.

## Hotfix Process Step by Step with Git and CI

### Backlog -> TODO

Same as for Features

### TODO -> Doing

Same as for Features except:

In  1.: Create a branch *from the most recent tag* instead of *master*, this will ensure you are branching from code that is on production.

### Doing -> Ready to Release

This differs significantly in that once the review is finished, you do NOT merge into master.  The ticket may sit here waiting to be released, but essentially nothing "happens" in this transition other than the reviewer approving it.

### Ready to Release -> Done

This will only release the specific hotfix.

1. In Jenkins click the "release hotfix" button (or "deploy hotfix") for the project, 
2. now all the steps ought to be performed by Jenkins that where done for the "release" job in Feature branches, with the one difference being that the **minor** version number ought to be bumped

Now at this point we have released the hotfix, but have not merged into master, so

8. Manually merge master into the hotfix branch to resolve any conflicts
9. Now run the "merge branch" jenkins job as mentioned in "Doing -> Ready to Release" for Features
10. Move the ticket to Done

# Monorepo vs Multirepo

The above flow assumes that for the given git repository there exists a single entry point to all tests, and a single entry point that builds a single artefact.  

## Multirepo

A multirepo, i.e. where each project corresponds to a separate git repo, solves the above assumption somewhat simply.  This means each repo has it's own corresponding Jenkins jobs.  The down side of multirepos is that releases to one project sometimes break other projects, and it's often the case that a single feature requires changes to many projects.  This creates a large development overhead and sometimes a manual deployment processs.

## Monorepo

In a monorepo one needs a script that runs all the test suites, and a script that builds all the artefacts then packages them into a single monoartefact.

It may be desirable when releasing to have a means to study which artefacts have actually changed, or even to limit which artefacts are expected to change.  For example suppose a release is expected to only change project A and project B but not project C, then when one performs the release they specify that they expect only A and B to change.  The build checks the hashes of the artefacts to ensure this.  Then the deploy job could lazily only deploy the artefacts that have changed.  For temporary clusters this would be implicit since the artefact it needs would be grabbed from s3 prior to the production run.

# Adversarial Pairing

TODO

# Epics

TODO
