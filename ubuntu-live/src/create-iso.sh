#!/bin/bash -xe

sudo apt-get -y install debootstrap

chmod -p target/work/chroot
cd target/work

sudo debootstrap --arch=i386 trusty chroot

sudo mount --bind /dev chroot/dev
sudo cp /etc/hosts chroot/etc/hosts
sudo cp /etc/resolv.conf chroot/etc/resolv.conf
sudo cp /etc/apt/sources.list chroot/etc/apt/sources.list

mkdir chroot/tmp/prepare

tee chroot/tmp/prepare/basic.sh 1> /dev/null 2>&1 <<-EOF
#!/bin/bash -xe
mount none -t proc /proc
mount none -t sysfs /sys
mount none -t devpts /dev/pts
export HOME=/root
export LC_ALL=C
apt-get update
apt-get install -y dbus
dbus-uuidgen > /var/lib/dbus/machine-id
dpkg-divert --local --rename --add /sbin/initctl
EOF
chmod +x chroot/tmp/prepare/basic.sh
sudo chroot chroot /tmp/prepare/basic.sh
