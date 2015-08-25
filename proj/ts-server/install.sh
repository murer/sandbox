#!/bin/bash -xe


sudo adduser --disabled-login --gecos ts ts
sudo chown -R ubuntu:ubuntu /home/ts

cd /home/ts

wget http://dl.4players.de/ts/releases/3.0.11.2/teamspeak3-server_linux-amd64-3.0.11.2.tar.gz

cd -
sudo chown -R ts:ts /home/ts
