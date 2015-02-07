#!/bin/bash -e

UNTIL="$1"

if [ "x$UNTIL" == "x" ]; then
	echo "Usage: $0 <until, like: ubuntu>"
	exit 1
fi

sudo docker images -a  | grep "$UNTIL" -m 1 -B 100 | tail -n +1 | head -n -1 | sed "s/\s\+/\t/g"| cut -f3 | xargs sudo docker rmi

