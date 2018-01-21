#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive

check_root() {
  [[ "$UID" == "0" ]]
}

clean() {
  rm -rf target/image || true
}

prepare_host() {
  apt-get install -y syslinux squashfs-tools genisoimage memtest86+
}

image_build() {
  mkdir -p target/image/casper target/image/isolinux target/image/install

  cp target/chroot/initrd.img target/image/casper/initrd.lz
  cp target/chroot/vmlinuz target/image/casper/vmlinuz

  cp /boot/memtest86+.bin target/image/install/memtest

  cp /usr/lib/syslinux/isolinux.bin target/image/isolinux/
  cp src/cdrom/conf/isolinux.txt target/image/isolinux
  cp src/cdrom/conf/isolinux.cfg target/image/isolinux
}

create_manifest() {
  chroot target/chroot dpkg-query -W --showformat='${Package} ${Version}\n' > \
    target/image/casper/filesystem.manifest
  cp target/image/casper/filesystem.manifest target/image/casper/filesystem.manifest-desktop
  for i in ubiquity ubiquity-frontend-gtk ubiquity-frontend-kde casper \
   lupin-casper live-initramfs user-setup discover1 xresprobe \
   os-prober libdebian-installer4; do
    sed -i "/${i}/d" target/image/casper/filesystem.manifest-desktop
  done
}

compress_chroot() {
  mksquashfs target/chroot target/image/casper/filesystem.squashfs
  du -sx --block-size=1 target/chroot | cut -f1 | \
    xargs echo -n > target/image/casper/filesystem.size
}

check_root
clean
prepare_host
image_build
create_manifest
compress_chroot
