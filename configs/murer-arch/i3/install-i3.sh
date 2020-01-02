#!/bin/bash -xe

basedir="$(dirname "$0")"
cd "$basedir"

sudo pacman --noconfirm -S i3-wm i3lock i3status xautolock

if [ ! -f /etc/i3.original.tar.gz ]; then
	cd /etc
 	sudo tar czf i3.original.tar.gz i3
 	sudo rm -rfv i3
 	cd -
fi

sudo cp -TRv etc/i3 /etc/i3

cp -v home/xinitrc ~/.xinitrc
# cp -v home/xinitrc ~/.xsessionrc
