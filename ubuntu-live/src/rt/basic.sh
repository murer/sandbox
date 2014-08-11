#!/bin/bash -xe
mount none -t proc /proc
mount none -t sysfs /sys
mount none -t devpts /dev/pts
export HOME=/root
export LC_ALL=C
apt-get update
apt-get install -y dbus
dbus-uuidgen > /var/lib/dbus/machine-id
dpkg-divert --local --rename --add /sbin/initctl

apt-get -y upgrade
apt-get install -y ubuntu-standard casper lupin-casper discover laptop-detect os-prober linux-generic
