#!/bin/bash
sudo apt-get install git
mkdir src
cd src
git clone https://github.com/OpenMaths/open-maths.git
git clone https://github.com/OpenMaths/open-maths-content.git
# Just in case
mkdir open-maths-content/json
mkdir open-maths-content/latex

# Will pull from a repo in future
mkdir user-db/actions
mkdir user-db/users


