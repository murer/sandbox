#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive

cd target/work

if [ ! -f iso-basic ]; then
	echo "iso-basic not found"
fi

cp ../../src/rt/close.sh chroot/tmp/prepare/close.sh
chmod +x chroot/tmp/prepare/close.sh
sudo chroot chroot /tmp/prepare/close.sh

while ! sudo umount chroot/dev; do sleep 1; done

cd -
