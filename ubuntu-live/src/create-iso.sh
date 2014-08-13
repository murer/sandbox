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


sudo rm image/casper/filesystem.squashfs | cat
sudo mksquashfs chroot image/casper/filesystem.squashfs -e boot
printf $(sudo du -sx --block-size=1 chroot | cut -f1) > image/casper/filesystem.size

cp ../../src/README.diskdefines image/README.diskdefines

touch image/ubuntu
mkdir image/.disk | cat
touch image/.disk/base_installable
echo "full_cd/single" > image/.disk/cd_type
echo "Ubuntu Remix" > image/.disk/info
echo "https://github.com/murer" > image/.disk/release_notes_url

cd -

cd target/work/image
find . -type f -print0 | xargs -0 md5sum | grep -v "\./md5sum.txt" | sudo tee md5sum.txt

sudo mkisofs -r -V "ubuntu-live" -cache-inodes -J -l -b isolinux/isolinux.bin -c isolinux/boot.cat -no-emul-boot -boot-load-size 4 -boot-info-table -o ../ubuntu-live.iso .

cd -


