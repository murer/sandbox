#!/bin/bash -xe

DEVICE=$1

if [ "x$DEVICE" == "x" ]; then
	echo "Usage: $0 </dev/sdX>"
	exit 1;
fi;

if [ ! -e "$DEVICE" ]; then
	echo "Device not found: $DEVICE";
	exit 1;
fi;

