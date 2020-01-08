#!/bin/bash -xe

```shell

sudo apt-get -y install cryptsetup debootstrap software-properties-common vim

# Configure lux patition on linux type partition
sudo cryptsetup -v -y --type luks2 --cipher aes-xts-plain64 --hash sha256 luksFormat /dev/sdXY

# open partition
sudo cryptsetup open /dev/sdXY ROOT

# format
sudo mkfs.ext4 /dev/mapper/ROOT
sudo mkfs.fat -F32 /dev/sdaEFI
sudo mkfs.ext4 /dev/sdBOOT

# mount root
sudo mkdir /mnt/installer
sudo mount /dev/mapper/ROOT /mnt/installer
sudo mkdir /mnt/installer/boot
sudo mount /dev/sdBOOT /mnt/installer/boot
sudo mkdir /mnt/installer/boot/efi
sudo mount /dev/sdEFI /mnt/installer/boot/efi

# debootstrap
sudo debootstrap bionic /mnt/installer

# sources.list
sudo tee /mnt/installer/etc/apt/sources.list <<-EOF
# See http://help.ubuntu.com/community/UpgradeNotes for how to upgrade to
# newer versions of the distribution.
deb http://us.archive.ubuntu.com/ubuntu/ bionic main restricted
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic main restricted

## Major bug fix updates produced after the final release of the
## distribution.
deb http://us.archive.ubuntu.com/ubuntu/ bionic-updates main restricted
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic-updates main restricted

## N.B. software from this repository is ENTIRELY UNSUPPORTED by the Ubuntu
## team. Also, please note that software in universe WILL NOT receive any
## review or updates from the Ubuntu security team.
deb http://us.archive.ubuntu.com/ubuntu/ bionic universe
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic universe
deb http://us.archive.ubuntu.com/ubuntu/ bionic-updates universe
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic-updates universe

## N.B. software from this repository is ENTIRELY UNSUPPORTED by the Ubuntu
## team, and may not be under a free licence. Please satisfy yourself as to
## your rights to use the software. Also, please note that software in
## multiverse WILL NOT receive any review or updates from the Ubuntu
## security team.
deb http://us.archive.ubuntu.com/ubuntu/ bionic multiverse
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic multiverse
deb http://us.archive.ubuntu.com/ubuntu/ bionic-updates multiverse
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic-updates multiverse

## N.B. software from this repository may not have been tested as
## extensively as that contained in the main release, although it includes
## newer versions of some applications which may provide useful features.
## Also, please note that software in backports WILL NOT receive any review
## or updates from the Ubuntu security team.
# deb http://us.archive.ubuntu.com/ubuntu/ bionic-backports main restricted universe multiverse
# deb-src http://us.archive.ubuntu.com/ubuntu/ bionic-backports main restricted universe multiverse

## Uncomment the following two lines to add software from Canonical's
## 'partner' repository.
## This software is not part of Ubuntu, but is offered by Canonical and the
## respective vendors as a service to Ubuntu users.
# deb http://archive.canonical.com/ubuntu bionic partner
# deb-src http://archive.canonical.com/ubuntu bionic partner

deb http://security.ubuntu.com/ubuntu bionic-security main restricted
# deb-src http://security.ubuntu.com/ubuntu bionic-security main restricted
deb http://security.ubuntu.com/ubuntu bionic-security universe
# deb-src http://security.ubuntu.com/ubuntu bionic-security universe
deb http://security.ubuntu.com/ubuntu bionic-security multiverse
# deb-src http://security.ubuntu.com/ubuntu bionic-security multiverse
EOF

# /etc/hosts
sudo tee /mnt/installer/etc/hosts <<-EOF
127.0.0.1 localhost
::1 localhost ip6-localhost ip6-loopback
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
127.0.0.1 ubuntu.localdomain ubuntu
EOF

# fstab
sudo apt-add-repository universe
sudo apt-get install -y arch-install-scripts
genfstab -U /mnt/installer | sudo tee /mnt/installer/etc/fstab

# config
echo "ubuntu" | sudo tee /mnt/installer/etc/hostname
echo 'LANG="en_US.UTF-8"' | sudo tee /mnt/installer/etc/default/locale
echo 'America/Sao_Paulo' | sudo tee /mnt/installer/etc/timezone
sudo cp /mnt/installer/usr/share/zoneinfo/America/Sao_Paulo /mnt/installer/etc/localtime
sudo arch-chroot /mnt/installer locale-gen en_US.UTF-8
sudo arch-chroot /mnt/installer dpkg-reconfigure -f non-interactive tzdata
sudo arch-chroot /mnt/installer apt-get -y update
sudo arch-chroot /mnt/installer apt-get -y install language-pack-en-base

# basics
sudo arch-chroot /mnt/installer apt-get -y install ubuntu-standard network-manager

# user murer 123
sudo arch-chroot /mnt/installer useradd -m -G sudo -s /bin/bash murer -p '$6$init$GyPRia4gtDwk6vb9hKUEvKBjZwIxodN7afESIJNKWv7REsPSA6IvdBoBjKCFdWNh9NpTDglTLN1fmTekbR9gN/'

# kernel
sudo arch-chroot /mnt/installer apt-get install -y linux-image-generic linux-headers-generic grub-efi cryptsetup

# grub
sudo sed -i.bak 's/^GRUB_CMDLINE_LINUX_DEFAULT=.*$/GRUB_CMDLINE_LINUX_DEFAULT="verbose nosplash"/g' /mnt/installer/etc/default/grub
echo "GRUB_ENABLE_CRYPTODISK=y" | sudo tee -a /mnt/installer/etc/default/grub
echo -e "ROOT\t/dev/sdLUKS\tnone\tluks" | sudo tee -a /mnt/installer/etc/crypttab
sudo arch-chroot /mnt/installer update-grub
sudo arch-chroot /mnt/installer grub-install /dev/sda

sudo umount -R /mnt/installer
```
