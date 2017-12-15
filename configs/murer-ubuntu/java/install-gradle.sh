#!/bin/bash -xe

grade_version=3.3

cd /opt
if [ ! -f gradle-$grade_version-bin.zip ]; then
	sudo wget "https://services.gradle.org/distributions/gradle-$grade_version-bin.zip"
fi
sudo rm -rf gradle-$grade_version | cat
sudo unzip gradle-$grade_version-bin.zip
if [ ! -L gradle ]; then
	sudo ln -s gradle-$grade_version gradle;
else
	echo 'maven link already exists. Maybe you want to change it';
fi;

if ! grep "bash.bashrc.gradle" /etc/bash.bashrc; then
	sudo tee /etc/bash.bashrc.gradle <<-EOF
	export GRADLE_HOME=/opt/gradle
	export PATH=\$GRADLE_HOME/bin:\$PATH
	EOF
	echo "# bash.bashrc.gradle" | sudo tee -a /etc/bash.bashrc
	echo "source /etc/bash.bashrc.gradle" | sudo tee -a /etc/bash.bashrc
fi

source /etc/bash.bashrc.gradle
cd -
