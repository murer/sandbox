#!/bin/bash -xe

rm /var/lib/dbus/machine-id

apt-get clean

rm -rf /tmp/*

umount -lf /proc
umount -lf /sys
umount -lf /dev/pts
