#!/bin/bash -xe

if [ ! -d target/work/chroot ]; then 
	sudo apt-get -y install debootstrap
	mkdir -p target/work/chroot
	cd target/work
	sudo debootstrap --arch=i386 trusty chroot
	cd -
fi;

cd target/work
sudo mount --bind /dev chroot/dev
sudo cp /etc/hosts chroot/etc/hosts
sudo cp /etc/resolv.conf chroot/etc/resolv.conf
sudo cp /etc/apt/sources.list chroot/etc/apt/sources.list

mkdir -p chroot/tmp/prepare | cat

if [ ! -f chroot/tmp/prepare/basic.sh ]; then
	cp ../../src/rt/basic.sh chroot/tmp/prepare/basic.sh
	chmod +x chroot/tmp/prepare/basic.sh
	sudo chroot chroot /tmp/prepare/basic.sh
fi;
