#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive

check_root() {
  [[ "$UID" == "0" ]]
}

clean() {
  rm -rf target/image || true
  rm -rf target/cdrom || true
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

config_ubunturemix() {
  cd target/image
  touch ubuntu
  mkdir .disk
  touch .disk/base_installable
  echo "full_cd/single" > .disk/cd_type
  echo "Ubuntu Remix 14.04" > .disk/info
  echo "http//your-release-notes-url.com" > .disk/release_notes_url
  find . -type f -print0 | xargs -0 md5sum | grep -v "\./md5sum.txt" > md5sum.txt
  cd ../..
}

create_iso() {
  mkdir target/cdrom
  cd target/image
  sudo mkisofs -r -V "ubuntu-live" -cache-inodes -J -l \
    -b isolinux/isolinux.bin -c isolinux/boot.cat \
    -no-emul-boot -boot-load-size 4 -boot-info-table \
    -o ../cdrom/ubuntu-remix.iso .
  cd ..
}

check_root
clean
prepare_host
image_build
create_manifest
compress_chroot
config_ubunturemix
create_iso

echo 'ISO Created'
