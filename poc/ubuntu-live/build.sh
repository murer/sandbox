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
}

chroot_prepare() {
  sed -e "s/CONF_HOSTNAME/$conf_hostname/g" conf/hosts > target/chroot/etc/hosts
  cp conf/resolv.conf target/chroot/etc/resolv.conf
  sed -e "s/CONF_UBUNTUNAME/$conf_ubuntuname/g" conf/sources.list > target/chroot/etc/apt/sources.list
  mkdir target/chroot/livebuild
  cp conf/chroot_build.sh target/chroot/livebuild/chroot_build.sh
  cp conf/chroot_clean.sh target/chroot/livebuild/chroot_clean.sh

  mount --bind /dev target/chroot/dev
  chroot target/chroot /bin/bash -xe /livebuild/chroot_build.sh
}

chroot_clean() {
  chroot target/chroot /bin/bash -xe /livebuild/chroot_clean.sh
  rm target/chroot/etc/resolv.conf
  rm -rf target/chroot/livebuild
  umount target/chroot/dev || true
}


check_root
load_conf
clean
bstrap
chroot_build
chroot_clean
