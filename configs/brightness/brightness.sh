#!/bin/bash -e

TO=$1

if [ "x$TO" == "x" ]; then
	sudo cat /sys/class/backlight/intel_backlight/brightness
else
	echo $TO | sudo tee /sys/class/backlight/intel_backlight/brightness
fi

