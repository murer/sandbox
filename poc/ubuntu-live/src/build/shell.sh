#!/bin/bash -xe

[[ "$UID" == "0" ]]

chroot target/chroot /bin/bash
