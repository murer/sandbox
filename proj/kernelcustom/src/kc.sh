#!/bin/bash -xe

#
# https://askubuntu.com/questions/1094854/how-to-modify-initrd-initial-ramdisk-of-ubuntu-18-10-cosmic-cuttlefish
#

workdir="$(cd "$(dirname "$0")/.." ; pwd -P)"

cmd_clean() {
  rm -rf target || true
}

cmd_download() {
  rm -rf target/original
  mkdir -p target/original
  scp -P 5022 "root@localhost:/boot/vmlinuz-4.15.0-48-generic" "target/original"
  scp -P 5022 "root@localhost:/boot/initrd.img-4.15.0-48-generic" "target/original"
  cp target/original/vmlinuz-4.15.0-48-generic target/original/vmlinuz-custom
  cp target/original/initrd.img-4.15.0-48-generic target/original/initrd-custom
}

cmd_shell() {
  ssh -p 5022 root@localhost
}

cmd_unpack() {
  rm -rf target/tmp || true
  mkdir -p target/tmp
  cd target/tmp
  #lzma -dc -S .lz ../original/initrd-custom | cpio -id
  gzip -dc ../original/initrd-custom | cpio -id
  cd -
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
