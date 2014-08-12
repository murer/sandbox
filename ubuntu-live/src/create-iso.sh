#!/bin/bash -xe

cd target/work

mkdir -p image/casper image/isolinux image/install

sudo apt-get install -y syslinux squashfs-tools genisoimage memtest86+

ls chroot/boot/vmlinuz-**.**.**-**-generic | sort | while read k; do sudo cp -v $k image/casper/vmlinuz; done
ls chroot/boot/initrd.img-**.**.**-**-generic | sort | while read k; do sudo cp -v $k image/casper/initrd.lz; done
cp /usr/lib/syslinux/isolinux.bin image/isolinux
cp /boot/memtest86+.bin image/install/memtest
cp ../../src/isolinux.txt image/isolinux
cp ../../src/isolinux.cfg image/isolinux

sudo chroot chroot dpkg-query -W --showformat='${Package} ${Version}\n' | sudo tee image/casper/filesystem.manifest
sudo cp -v image/casper/filesystem.manifest image/casper/filesystem.manifest-desktop
for i in ubiquity ubiquity-frontend-gtk ubiquity-frontend-kde casper lupin-casper live-initramfs user-setup discover1 xresprobe os-prober libdebian-installer4; do
	sudo sed -i "/${i}/d" image/casper/filesystem.manifest-desktop
done


cd -


