#!/bin/bash -xe 

cd /opt
if [ ! -f apache-maven-3.3.3-bin.zip ]; then
	sudo wget http://ftp.unicamp.br/pub/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.zip
fi
sudo rm -rf apache-maven-3.3.3 | cat
sudo unzip apache-maven-3.3.3-bin.zip
if [ ! -L maven ]; then
	sudo ln -s apache-maven-3.3.3 maven;
else
	echo 'maven link already exists. Maybe you want to change it';
fi;

if ! grep "bash.bashrc.maven" /etc/bash.bashrc; then
	sudo tee /etc/bash.bashrc.maven <<-EOF
	export MAVEN_HOME=/opt/maven
	export PATH=\$MAVEN_HOME/bin:\$PATH
	EOF
	echo "# bash.bashrc.maven" | sudo tee -a /etc/bash.bashrc
	echo "source /etc/bash.bashrc.maven" | sudo tee -a /etc/bash.bashrc
fi

source /etc/bash.bashrc.maven
cd -
