#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive
export HOME=/root
export LC_ALL=C

apt-get install git

[[ -d /livebuild/sandbox ]] || git clone https://github.com/murer/sandbox /livebuild/sandbox

cd /livebuild/sandbox/configs/murer-ubuntu
cd basics
./install-basics.sh
cd ..
cd xorg
./install-xorg.sh
cd ..
cd wicd
./install-wicd.sh
cd ..
cd graphics-util
./install-graphics-util.sh
cd ..
cd openbox
./install-openbox.sh
cd /

wget --no-check-certificate -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | sudo tee /etc/apt/sources.list.d/google-chrome.list
sudo apt-get update
sudo apt-get -y install google-chrome-stable
