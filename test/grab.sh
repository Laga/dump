#!/bin/bash
rm setup.sh && wget https://raw.githubusercontent.com/samthebest/dump/master/test/setup.sh && chmod +x setup.sh && ./setup.sh

rm launch.sh && wget https://raw.githubusercontent.com/samthebest/dump/master/test/launch.sh && chmod +x launch.sh && ./launch.sh prod
