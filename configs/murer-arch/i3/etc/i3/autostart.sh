#!/bin/bash -xe

xautolock -time 5 -locker /etc/i3/lock.sh &

nm-applet &
