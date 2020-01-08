```shell

sudo cryptsetup open /dev/sda1 LVM

sudo pvcreate /dev/mapper/LVM

sudo vgcreate MAIN /dev/mapper/LVM

sudo pvdisplay
sudo pvscan

sudo lvcreate -L 8G MAIN -n SWAP
sudo lvcreate -l '100%FREE' MAIN -n ROOT

sudo lvdisplay
sudo lvscan

```
