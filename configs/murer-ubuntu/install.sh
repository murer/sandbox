#!/bin/bash -xe

cd xorg
./install-xorg.sh
cd -
cd network-manager
./install-network-manager.sh
cd -
cd graphics-util
./install-graphics-util.sh
cd -
#cd openbox
#./install-openbox.sh
#cd -
cd i3
./install-i3.sh
cd -
cd chrome
./install-chrome.sh
cd -
cd slim
#./install-slim.sh
cd -
