```shell

sudo cryptsetup open /dev/sda2 CRYPTED

sudo pvcreate /dev/mapper/CRYPTED

sudo vgcreate MAIN /dev/mapper/CRYPTED

sudo pvdisplay
sudo pvscan

sudo lvcreate -L 4G MAIN -n SWAP
sudo lvcreate -l '100%FREE' MAIN -n ROOT

sudo lvdisplay
sudo lvscan

# format parts
sudo mkfs.fat -n ESP -F32 /dev/sda1
sudo mkfs.ext4 -L ROOT /dev/mapper/MAIN-ROOT
sudo mkswap -L SWAP /dev/mapper/MAIN-SWAP

# mount install env
sudo swapon /dev/mapper/MAIN-SWAP
sudo mkdir -p /mnt/installer
sudo mount /dev/mapper/MAIN-ROOT /mnt/installer
sudo mkdir -p /mnt/installer/boot/efi
sudo mount /dev/sda1 /mnt/installer/boot/efi

# strap and install things

_DISK_UUID_LUKS="$(sudo blkid -o value -s UUID /dev/sda2)"
_DISK_UUID_ROOT="$(sudo blkid -o value -s UUID /dev/mapper/MAIN-ROOT)"
_DISK_UUID_SWAP="$(sudo blkid -o value -s UUID /dev/mapper/MAIN-SWAP)"

sudo arch-chroot /mnt/installer apt-get install -y lvm2
echo -e "CRYPTED\tUUID=$_DISK_UUID_LUKS\tnone\tluks,initramfs" | sudo tee -a /mnt/installer/etc/crypttab

# default grub
sudo sed -i.original 's/^\(GRUB_CMDLINE_LINUX_DEFAULT\)=.*$/\1="verbose nosplash"/g' /mnt/installer/etc/default/grub
# GRUB_CMDLINE_LINUX_DEFAULT="verbose nosplash"
# GRUB_CMDLINE_LINUX="cryptdevice=UUID=6fead3ef-442b-4af5-b3b1-d126fc3eec38:LVM root=/dev/mapper/MAIN-ROOT"
# GRUB_ENABLE_CRYPTODISK=y
# GRUB_PRELOAD_MODULES="cryptodisk luks"

echo 'CRYPTSETUP=y' | sudo tee -a /mnt/installer/etc/cryptsetup-initramfs/conf-hook

```
