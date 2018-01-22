#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive
export HOME=/root
export LC_ALL=C

sudo apt-get -y \
  install git subversion nmap netcat vim curl wget \
  pv zip connect-proxy tcpdump zip pv bc \
  wicd wicd-curses wicd-gtk \
  firefox gedit gmrun lxterminal gedit gnome-screenshot pcmanfm \
  openbox tint2 xfce4-power-manager xscreensaver arandr

if [ ! -f /etc/X11/openbox.original.tar.gz ]; then
	cd /etc/X11
	sudo tar czf openbox.original.tar.gz openbox/*
	sudo rm -rfv openbox/*
	cd -
fi
sudo cp -Rv /livebuild/conf/openbox/* /etc/X11/openbox

wget --no-check-certificate -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | sudo tee /etc/apt/sources.list.d/google-chrome.list
sudo apt-get update
sudo apt-get -y install google-chrome-stable

cp /livebuild/conf/network.conf /etc/network/interfaces
