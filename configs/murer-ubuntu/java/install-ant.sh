#!/bin/bash -xe 

VERSION=1.9.6

cd /opt
if [ ! -f apache-ant-$VERSION-bin.zip ]; then
	sudo wget http://ftp.unicamp.br/pub/apache//ant/binaries/apache-ant-$VERSION-bin.zip
fi
sudo rm -rf apache-ant-$VERSION | cat
sudo unzip apache-ant-$VERSION-bin.zip
if [ ! -L ant ]; then
	sudo ln -s apache-ant-$VERSION ant;
else
	echo 'ant link already exists. Maybe you want to change it';
fi;

if ! grep "bash.bashrc.ant" /etc/bash.bashrc; then
	sudo tee /etc/bash.bashrc.ant <<-EOF
	export ANT_HOME=/opt/ant
	export PATH=\$ANT_HOME/bin:\$PATH
	EOF
	echo "# bash.bashrc.ant" | sudo tee -a /etc/bash.bashrc
	echo "source /etc/bash.bashrc.ant" | sudo tee -a /etc/bash.bashrc
fi

source /etc/bash.bashrc.ant
cd -
