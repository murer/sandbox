#!/bin/bash -xe

cp /etc/pacman.d/mirrorlist /etc/pacman.d/mirrorlist.bak
cat /etc/pacman.d/mirrorlist.bak | grep '## Brazil' -A 1 | grep -v '\-\-' > /tmp/mirrors.selected.txt
cat /tmp/mirrors.selected.txt /etc/pacman.d/mirrorlist.bak > /etc/pacman.d/mirrorlist
