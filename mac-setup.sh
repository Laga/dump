#!/bin/bash
# MAC SETUP SCRIPT

# Change editor (for next step):
export EDITOR=nano
echo "export EDITOR=nano" >> ~/.bash_profile

# Increase bash history to a million commands (should be enough)
echo "HISTFILESIZE=10000000" >> ~/.bash_profile

# Change prompt to linux default
echo "export PS1='$(whoami)@$(hostname):$(pwd) '" >> ~/.bash_profile

# Install brew
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

# Install brew cask (makes it easier to install mac apps)
brew install caskroom/cask/brew-cask

# Spectacle means you can resize and move windows with shortcuts (requires manual step afterwards)
brew cask install spectacle

# Install sublime (best text editor ever!)
brew cask install sublime-text

# Setup a link so you can run sublime from command line
ln -s /Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl /usr/local/bin/sublime

# Install java
brew cask install java

# Install scala
brew install scala

# Install sbt
brew install sbt

# Install Intellij (Community edition)
brew cask install intellij-idea-ce

# ifstat
brew install ifstat

# firefox
brew cask install firefox

# git-flow
brew install git-flow

# git auto completion
brew install git bash-completion
# add bash-completion to ~/.bash_profile
echo "if [ -f \$(brew --prefix)/etc/bash_completion ]; then" >> ~/.bash_profile
echo "    . \$(brew --prefix)/etc/bash_completion" >> ~/.bash_profile
echo "fi"  >> ~/.bash_profile
# Then start git auto completion
if [ -f $(brew --prefix)/etc/bash_completion ]; then
    . $(brew --prefix)/etc/bash_completion
fi

# spotify for concentration
brew cask install spotify

# open office
brew cask install openoffice

# wget
brew install wget
