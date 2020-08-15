#!/bin/bash -xe

[[ "x$UID" != "x0" ]]

sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

echo "deb http://download.virtualbox.org/virtualbox/debian $(lsb_release -cs) contrib" | sudo tee /etc/apt/sources.list.d/virtualbox.list
echo "# deb-src http://download.virtualbox.org/virtualbox/debian $(lsb_release -cs) contrib" | sudo tee /etc/apt/sources.list.d/virtualbox.list

# sudo apt-get -y update
# sudo apt-get install -y docker-ce docker-ce-cli containerd.io
# sudo docker run hello-world
# sudo usermod -aG docker "$USER"
