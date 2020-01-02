#!/bin/bash -xe

echo 'en_US.UTF-8 UTF-8' >> /mnt/etc/locale.gen
echo 'LANG=en_US.UTF-8' > /mnt/etc/locale.conf
echo 'KEYMAP=br-abnt2' > /mnt/etc/vconsole.conf
echo 'lavaburst' > /mnt/etc/hostname

arch-chroot /mnt ln -sf /usr/share/zoneinfo/Brazil/West /etc/localtime
arch-chroot /mnt locale-gen
#arch-chroot /mnt localectl --no-convert set-x11-keymap br
arch-chroot /mnt pacman --noconfirm -S wpa_supplicant refind-efi networkmanager binutils sudo vim

mkdir -p /mnt/esp/EFI/Boot
cp /mnt/usr/share/refind/refind_x64.efi /mnt/esp/EFI/Boot/bootx64.efi
cp -r /mnt/usr/share/refind/drivers_x64/ /mnt/esp/EFI/Boot/
echo 'extra_kernel_version_strings linux,linux-hardened,linux-lts,linux-zen,linux-git;' > /mnt/esp/EFI/Boot/refind.conf
echo 'fold_linux_kernels false' >> /mnt/esp/EFI/Boot/refind.conf
echo 'default_selection "linux from"' >> /mnt/esp/EFI/Boot/refind.conf

arch-chroot /mnt useradd -m -G wheel -s /bin/bash murer
echo 'murer:123' | arch-chroot /mnt chpasswd murer
arch-chroot /mnt perl -i -pe 's/# (%wheel ALL=\(ALL\) ALL)/$1/' /etc/sudoers

arch-chroot /mnt passwd -l root

umount -R /mnt

# arch-chroot /mnt
# LANG=C perl -i -pe 's/#(en_US.UTF)/$1/' /etc/locale.gen
# LANG=C perl -i -pe 's/#(de_CH.UTF)/$1/' /etc/locale.gen
# locale-gen
# echo 'LANG=en_US.UTF-8' > /etc/locale.conf
# echo 'KEYMAP=de_CH-latin1' > /etc/vconsole.conf
# localectl --no-convert set-x11-keymap ch
# ln -sf /usr/share/zoneinfo/Brazil/West /etc/localtime
# echo $myhostname > /etc/hostname
# pacman -S dialog wpa_supplicant refind-efi
# mkdir -p /esp/EFI/Boot
# cp /usr/share/refind/refind_x64.efi /esp/EFI/Boot/bootx64.efi
# cp -r /usr/share/refind/drivers_x64/ /esp/EFI/Boot/
# echo 'extra_kernel_version_strings linux,linux-hardened,linux-lts,linux-zen,linux-git;' > /esp/EFI/Boot/refind.conf
# echo 'fold_linux_kernels false' >> /esp/EFI/Boot/refind.conf
# echo 'default_selection "linux from"' >> /esp/EFI/Boot/refind.conf
