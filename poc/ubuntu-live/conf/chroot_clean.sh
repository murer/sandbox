#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive
export HOME=/root
export LC_ALL=C

rm /var/lib/dbus/machine-id

apt-get clean

rm -rf /tmp/*

umount -lf /proc
umount -lf /sys
umount -lf /dev/pts
