#!/bin/bash -xe
export DEBIAN_FRONTEND=noninteractive
export HOME=/root
export LC_ALL=C
if ! mount | grep /proc; then mount none -t proc /proc; fi;
if ! mount | grep /sys; then mount none -t sysfs /sys; fi;
if ! mount | grep /dev/pts; then mount none -t devpts /dev/pts; fi;
#apt-get update
sudo apt-get install -y dbus
dbus-uuidgen > /var/lib/dbus/machine-id
dpkg-divert --local --rename --add /sbin/initctl

sudo apt-get -y upgrade
sudo apt-get install -y ubuntu-standard casper lupin-casper
sudo apt-get install -y discover laptop-detect os-prober
sudo apt-get install -y linux-generic
