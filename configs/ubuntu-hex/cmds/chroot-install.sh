#!/bin/bash -xe

[[ "x$UID" == "x0" ]]

cd "$(dirname "$0")/.."
pwd

cp -R target/config/etc/* /mnt/installer/etc

arch-chroot /mnt/installer ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime
arch-chroot /mnt/installer locale-gen en_US.UTF-8
arch-chroot /mnt/installer dpkg-reconfigure -f non-interactive tzdata
arch-chroot /mnt/installer apt-get -y update
arch-chroot /mnt/installer apt-get -y install \
  ubuntu-standard \
  language-pack-en-base \
  network-manager \
  linux-image-generic linux-headers-generic \
  grub-efi cryptsetup lvm2

sudo tee /mnt/installer/etc/netplan/01-netcfg.yaml <<-EOF
network:
  version: 2
  renderer: NetworkManager
EOF
