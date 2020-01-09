```shell

sudo cryptsetup open /dev/sdXY CRYPTED

sudo pvcreate /dev/mapper/CRYPTED

sudo vgcreate MAIN /dev/mapper/CRYPTED

sudo pvdisplay
sudo pvscan

sudo lvcreate -L 8G MAIN -n SWAP
sudo lvcreate -l '100%FREE' MAIN -n ROOT

sudo lvdisplay
sudo lvscan

# format parts
sudo mkfs.fat -F32 /dev/sda1
sudo mkfs.ext4 /dev/mapper/MAIN-ROOT

# mount install env
sudo mkdir -p /mnt/installer
sudo mount /dev/mapper/MAIN-ROOT /mnt/installer
sudo mkdir -p /mnt/installer/boot/efi
sudo mount /dev/sda1 /mnt/installer/boot/efi

# strap and instasll things

sudo arch-chroot /mnt/installer apt-get install -y lvm2
echo -e "CRYPTED\tUUID=[ID DO /dev/sdXY encryptado]\tnone\tluks,discard" | sudo tee -a /mnt/installer/etc/crypttab

# default grub
# GRUB_CMDLINE_LINUX_DEFAULT="verbose nosplash"
# GRUB_CMDLINE_LINUX="cryptdevice=UUID=6fead3ef-442b-4af5-b3b1-d126fc3eec38:LVM root=/dev/mapper/MAIN-ROOT"
# GRUB_ENABLE_CRYPTODISK=y
# GRUB_PRELOAD_MODULES="cryptodisk luks"

echo 'CRYPTSETUP=y' | sudo tee -a /mnt/installer/etc/cryptsetup-initramfs/conf-hook

```
