#!/bin/bash -xe

sudo pacman --noconfirm -S xorg-server xorg-xinit
sudo localectl --no-convert set-x11-keymap br

