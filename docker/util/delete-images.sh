#!/bin/bash -e

GREP=$1

if [ "x$GREP" == "x" ]; then
	echo "usage: $0 <grep-pattern>"
	exit 1;
fi

IMAGES=$(sudo docker images --no-trunc | grep "$GREP" | sed "s/\s\+/\t/g")
echo "We will delete these images:"
echo -e "$IMAGES"
echo "Confirm? <no>";

read resp
if [ "yes" == "$resp" ] || [ "y" == "$resp" ]; then
	echo "$IMAGES" | cut -f3 | xargs sudo docker rmi 
fi;

