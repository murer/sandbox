#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive
export HOME=/root
export LC_ALL=C

mount none -t proc /proc
mount none -t sysfs /sys
mount none -t devpts /dev/pts

apt-get -y update
apt-get install --yes dbus
dbus-uuidgen > /var/lib/dbus/machine-id
#dpkg-divert --local --rename --add /sbin/initctl

#apt-get -y upgrade
apt-get install -y ubuntu-standard casper lupin-casper
apt-get install -y discover laptop-detect os-prober
apt-get install -y linux-generic

rm /var/lib/dbus/machine-id
apt-get clean
rm -rf /tmp/*
umount -lf /proc
umount -lf /sys
umount -lf /dev/pts
