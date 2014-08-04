#!/bin/bash -xe

sudo apt-get install openbox nitrogen tint2 xfce4-power-manager volti xscreensaver gnome-keyring

if [ ! -f /etc/X11/openbox.original.tar.gz ]; then
	cd /etc/X11
	sudo tar czf openbox.original.tar.gz openbox
	sudo rm -rfv openbox
	cd -
fi

sudo cp -Rv etc/X11/openbox /etc/X11
