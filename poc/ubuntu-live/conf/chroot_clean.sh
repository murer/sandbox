#!/bin/bash -xe

rm /var/lib/dbus/machine-id

apt-get clean

rm -rf /tmp/*

rm /etc/resolv.conf

umount -lf /proc
umount -lf /sys
umount -lf /dev/pts
