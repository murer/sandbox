#!/bin/bash -xe

sudo pacman --noconfirm -S virtualbox virtualbox-host-modules-arch
sudo modprobe vboxdrv
