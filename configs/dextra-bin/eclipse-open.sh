#!/bin/bash

while [ "x$1" != "x" ]; do
	FILE="$1"
	FIRST="${FILE:0:1}"
	if [ "x$FIRST" != "x/" ] && [ "x$FIRST" != "x~" ]; then
		FILE="$(pwd)/$FILE"
	fi
	/home/murer/opt/eclipse/eclipse --launcher.openFile "$FILE"
	shift
done

