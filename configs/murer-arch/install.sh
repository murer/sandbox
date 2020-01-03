#!/bin/bash -xe

cd basics
./install-basics.sh
./install-audio.sh
cd -
cd xorg
./install-xorg.sh
cd -
cd graphics-util
./install-graphics-util.sh
cd -
cd i3
./install-i3.sh
cd -
