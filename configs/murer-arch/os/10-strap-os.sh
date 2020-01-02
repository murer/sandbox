#!/bin/bash -xe

pacstrap /mnt base linux
genfstab -U /mnt >> /mnt/etc/fstab
