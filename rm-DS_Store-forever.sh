echo "while true; do find / -name .DS_Store -exec echo \"{}\" \; ; sleep 2; done" > ~/.rm-DS_Store-abomination.sh
chmod +x ~/.rm-DS_Store-abomination.sh


screen -ls | grep rm-DS_Store-abomination >/dev/null || screen -S rm-DS_Store-abomination -d -m ~/.rm-DS_Store-abomination.sh
