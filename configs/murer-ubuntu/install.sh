#!/bin/bash -xe

cd xorg
./install-xorg.sh
cd -
cd wicd
./install-wicd.sh
cd -
cd graphics-util
./install-graphics-util.sh
cd -
cd openbox
./install-openbox.sh
cd -
cd chrome
./install-chrome.sh
cd -
cd slim
#./install-slim.sh
cd -

