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

cmd_delete() {
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
  mkdir -p target/home
  vm_from_disk="$(vboxmanage showvminfo hexblade1 --machinereadable | grep SATA-ImageUUID-0-0 | cut -d"=" -f2 | cut -d'"' -f2)"
  vboxmanage clonemedium disk "$vm_from_disk" "target/root.vdi" --format VDI
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
