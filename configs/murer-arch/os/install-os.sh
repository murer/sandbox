#!/bin/bash -xe

pacstrap /mnt base linux
genfstab -U /mnt >> /mnt/etc/fstab
arch-chroot /mnt
LANG=C perl -i -pe 's/#(en_US.UTF)/$1/' /etc/locale.gen
echo 'LANG=en_US.UTF-8' > /etc/locale.conf
echo 'KEYMAP=br-abnt2' > /etc/vconsole.conf
locale-gen
localectl --no-convert set-x11-keymap ch
ln -sf /usr/share/zoneinfo/Brazil/West /etc/localtime
echo lavaburst > /etc/hostname
pacman -S dialog wpa_supplicant refind-efi
mkdir -p /esp/EFI/Boot
cp /usr/share/refind/refind_x64.efi /esp/EFI/Boot/bootx64.efi
cp -r /usr/share/refind/drivers_x64/ /esp/EFI/Boot/
echo 'extra_kernel_version_strings linux,linux-hardened,linux-lts,linux-zen,linux-git;' > /esp/EFI/Boot/refind.conf
echo 'fold_linux_kernels false' >> /esp/EFI/Boot/refind.conf
echo 'default_selection "linux from"' >> /esp/EFI/Boot/refind.conf
