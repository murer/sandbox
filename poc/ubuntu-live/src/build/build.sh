#!/bin/bash -xe

conf_action="${1?'Use preapre, customize, cleanup or drop'}"

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

drop() {
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

update_livebuild() {
  rm -rf target/chroot/livebuild || true
  cp -R src/build/helper target/chroot/livebuild
}

chroot_prepare() {
  sed -e "s/CONF_HOSTNAME/$conf_hostname/g" src/build/conf/hosts > target/chroot/etc/hosts
  cp src/build/conf/resolv.conf target/chroot/etc/resolv.conf
  sed -e "s/CONF_UBUNTUNAME/$conf_ubuntuname/g" src/build/conf/sources.list > target/chroot/etc/apt/sources.list
  update_livebuild

  mount --bind /dev target/chroot/dev
  chroot target/chroot /bin/bash -xe /livebuild/chroot_prepare.sh
  echo 'Preapred'
}

chroot_customize() {
  update_livebuild
  chroot target/chroot /bin/bash -xe /livebuild/chroot_customize.sh
  echo 'Customized'
}

chroot_cleanup() {
  chroot target/chroot /bin/bash -xe /livebuild/chroot_cleanup.sh
  umount target/chroot/dev
  rm -rf target/chroot/livebuild
  echo 'Cleaned up'
}

check_root
load_conf

case "$conf_action" in
  bootstrap)
    drop
    bstrap
    ;;
  prepare)
    chroot_prepare
    ;;
  customize)
    chroot_customize
    ;;
  cleanup)
    chroot_cleanup
    ;;
  drop)
    drop
    ;;
  *)
    echo $"Usage: $0 {bootstrap|prepare|customize|cleanup}"
    exit 1
esac
