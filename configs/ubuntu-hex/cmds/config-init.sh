#!/bin/bash -xe

[[ "x$UID" != "x0" ]]

cd "$(dirname "$0")/.."
pwd

hex_lvm_id="$(sudo blkid -o value -s UUID "$HEX_DEV_LVM")"

read -p 'Hostname [hex]: ' hex_hostname
hex_hostname="${hex_hostname:-hex}"

rm -rf target || true
mkdir target
cp -R config target

echo "$hex_hostname" > target/config/etc/hostname

echo -e "CRYPTED\tUUID=$hex_lvm_id\tnone\tluks,initramfs" > target/config/etc/crypttab

echo "
127.0.0.1 localhost
::1 localhost ip6-localhost ip6-loopback
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
127.0.0.1 $hex_hostname.localdomain $hex_hostname
" > target/config/etc/hosts
