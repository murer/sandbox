#!/bin/bash -xe

cmd_create() {
  vm_name="${1?'vm_name'}"

  mkdir -p "target/vm/$vm_name"

  if vboxmanage showvminfo "$vm_name" > /dev/null; then false; fi

  vboxmanage createhd --filename "target/vm/$vm_name/root.vdi" --format VDI --diffparent "target/root.vdi"
  vboxmanage createvm --name "$vm_name" \
    --ostype Ubuntu_64 \
    --basefolder "$(pwd)/target/vm" \
    --register
  vboxmanage modifyvm "$vm_name" --memory "2048"


  # vboxmanage clonevm "hexblade1" \
  #   --register \
  #   --snapshot Installed \
  #   --options link \
  #   --name "$vm_name"


  #vboxmanage clonemedium disk

}

cmd_vm_full() {
  vm_name="${1?'vm_name'}"

  mkdir -p "target/vm/$vm_name"

  if vboxmanage showvminfo "$vm_name" > /dev/null; then false; fi

  vboxmanage clonevm "hexblade1" \
    --register \
    --snapshot Installed \
    --name "$vm_name"
  
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-1-0 | cut -d"=" -f2 | cut -d'"' -f2)"
}

cmd_vm_link() {
  vm_name="${1?'vm_name'}"

  mkdir -p "target/vm/$vm_name"

  if vboxmanage showvminfo "$vm_name" > /dev/null; then false; fi

  vboxmanage clonevm "hexblade1" \
    --register \
    --snapshot Installed \
    --options link \
    --name "$vm_name"
  
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-1-0 | cut -d"=" -f2 | cut -d'"' -f2)"
}

cmd_vm_delete() {
  vm_name="${1?'vm_name'}"
  vboxmanage unregistervm "$vm_name" --delete
}

cmd_home_create() {
  vm_name="${1?'vm_name'}"
  mkdir -p target/home
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-1-0 | cut -d"=" -f2 | cut -d'"' -f2)"
  vboxmanage clonemedium disk "$vm_from_disk" "target/home/$vm_name.vdi" --format VDI
}

cmd_root_clone() {
  mkdir -p target/root
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-0-0 | cut -d"=" -f2 | cut -d'"' -f2)"
  vboxmanage clonemedium disk "$vm_from_disk" "target/root/root.vdi" --format VDI
}

cmd_root_link() {
  mkdir -p target/root
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-0-0 | cut -d"=" -f2 | cut -d'"' -f2)"
  vboxmanage createmedium disk --filename "target/root/root.vdi" --format VDI --diffparent "$vm_from_disk"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
