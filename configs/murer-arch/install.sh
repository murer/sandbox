#!/bin/bash -xe

cd basics
./install-basics.sh
./install-audio.sh
./install-xorg.sh
./install-graphics-util.sh
cd -
cd i3
./install-i3.sh
cd -
