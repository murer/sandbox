#!/bin/bash -xe

check_root() {
  [[ "$UID" == "0" ]]
}

load_conf() {
  conf_hostname="$(cat /etc/hostname)"
  conf_ubuntuname="$(lsb_release -cs)"
  conf_arch=$(uname -m)
  [[ "$conf_arch" == "x86_64" ]] && conf_arch=amd64
}

clean() {
  umount target/chroot/dev || true
  rm -rf target || true
}

bstrap() {
  mkdir -p target/chroot
  debootstrap "--arch=$conf_arch" "$conf_ubuntuname" target/chroot
  mount --bind /dev target/chroot/dev
}

chroot_prepare() {
  sed -e "s/CONF_HOSTNAME/$conf_hostname/g" conf/hosts > target/chroot/etc/hosts
  cp conf/resolv.conf target/chroot/etc/resolv.conf
  sed -e "s/CONF_UBUNTUNAME/$conf_ubuntuname/g" conf/sources.list > target/chroot/etc/apt/sources.list
  cp conf/chroot_build.sh target/chroot/chroot_build.sh
  cp conf/chroot_clean.sh target/chroot/chroot_clean.sh
}

chroot_build() {
  chroot target/chroot /bin/bash -xe /chroot_build.sh
}

chroot_clean() {
  chroot target/chroot /bin/bash -xe /chroot_clean.sh
}


check_root
load_conf
#clean
#bstrap
chroot_prepare
chroot_build
