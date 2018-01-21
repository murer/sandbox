#!/bin/bash -xe

conf_action="${1?'Use preapre, customize or cleanup'}"

export DEBIAN_FRONTEND=noninteractive

check_root() {
  [[ "$UID" == "0" ]]
}

load_conf() {
  conf_hostname="$(cat /etc/hostname)"
  conf_ubuntuname="$(lsb_release -cs)"
  conf_arch=$(uname -m)
  [[ "$conf_arch" == "x86_64" ]] && conf_arch=amd64
}

chroot_cleanup() {
  chroot target/chroot umount -lf /proc || true
  chroot target/chroot umount -lf /sys  || true
  chroot target/chroot umount -lf /dev/pts  || true
  umount target/chroot/dev || true
  rm -rf target/chroot || true
}

bstrap() {
  mkdir -p target/chroot
  debootstrap "--arch=$conf_arch" "$conf_ubuntuname" target/chroot
}

chroot_prepare() {
  sed -e "s/CONF_HOSTNAME/$conf_hostname/g" src/build/conf/hosts > target/chroot/etc/hosts
  cp src/build/conf/resolv.conf target/chroot/etc/resolv.conf
  sed -e "s/CONF_UBUNTUNAME/$conf_ubuntuname/g" src/build/conf/sources.list > target/chroot/etc/apt/sources.list
  mkdir target/chroot/livebuild
  cp src/build/helper/chroot_build.sh target/chroot/livebuild/chroot_build.sh

  mount --bind /dev target/chroot/dev
  chroot target/chroot /bin/bash -xe /livebuild/chroot_prepare.sh
}

check_root
load_conf

case "$conf_action" in
  prepare)
    chroot_cleanup
    bstrap
    chroot_prepare
    ;;
  customize)
    chroot_customize
    ;;
  cleanup)
    chroot_cleanup
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart|condrestart|status}"
    exit 1
esac
