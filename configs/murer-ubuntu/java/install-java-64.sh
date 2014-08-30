#!/bin/bash -xe 

cd /opt
if [ ! -f jdk-8u20-linux-x64.tar.gz ]; then
	sudo wget https://repoz.dextra.com.br/repoz/r/pub/jdk/oracle/jdk-8u20-linux-x64.tar.gz
fi
sudo rm -rf jdk1.8.0_20 | cat
sudo tar xzvf jdk-8u20-linux-x64.tar.gz
if [ ! -L jdk ]; then
	sudo ln -s jdk1.8.0_20 jdk;
else
	echo 'jdk link already exists. Maybe you want to change it';
fi;

if ! grep "bash.bashrc.jdk" /etc/bash.bashrc; then
	sudo tee /etc/bash.bashrc.jdk <<-EOF
	export JAVA_HOME=/opt/jdk
	export PATH=\$JAVA_HOME/bin:\$PATH
	EOF
	echo "# bash.bashrc.jdk" | sudo tee -a /etc/bash.bashrc
	echo "source /etc/bash.bashrc.jdk" | sudo tee -a /etc/bash.bashrc
fi

source /etc/bash.bashrc.jdk

cd -

