#!/bin/bash -xe


sudo adduser --disabled-login --gecos ts ts
sudo chown -R ubuntu:ubuntu /home/ts

cd /home/ts

wget http://dl.4players.de/ts/releases/3.0.11.2/teamspeak3-server_linux-amd64-3.0.11.2.tar.gz
tar xzf teamspeak3-server_linux-amd64-3.0.11.2.tar.gz
mkdir opt
mv teamspeak3-server_linux-amd64 opt/teamspeak
sudo ln -s /home/ts/opt/teamspeak/ts3server_startscript.sh /etc/init.d/teamspeak
sudo update-rc.d teamspeak defaults
sudo service teamspeak start

cd -
sudo chown -R ts:ts /home/ts
