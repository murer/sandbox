# Arch Linux

### Patitions

Restart partition table, so you can choose GPT

*** It is about to destroy all parititions ***

```shell
cfdisk -z /dev/sdX # DANGEROUS
# Choose GPT
```

Partitions sample

```shell
gdisk -l /dev/sda
```

```
GPT fdisk (gdisk) version 1.0.4

Partition table scan:
  MBR: protective
  BSD: not present
  APM: not present
  GPT: present

Found valid GPT with protective MBR; using GPT.
Disk /dev/sda: 500118192 sectors, 238.5 GiB
Model: LITEON CV8-8E256
Sector size (logical/physical): 512/512 bytes
Disk identifier (GUID): EA013B50-6609-4538-BB39-DB8003BCC99F
Partition table holds up to 128 entries
Main partition table begins at sector 2 and ends at sector 33
First usable sector is 34, last usable sector is 500118158
Partitions will be aligned on 2048-sector boundaries

Number  Start (sector)    End (sector)  Size       Code  Name
   1            2048          514047   250.0 MiB   EF00  EFI System
   2          514048       147314687   70.0 GiB    8300  Linux filesystem
   5       147314688       164091903   8.0 GiB     8200  Linux swap
```

Format

```shell
mkfs.fat -F32 /dev/sdXY # EFI System
mkfs.ext4 /dev/sdXY # Root partition
```

Mount

```shell
mount /dev/sdXY /mnt # Root Partition
mkdir -p /mnt/esp
mount /dev/sdXY /mnt/esp # EFI System partition
```
