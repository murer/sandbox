#!/bin/bash -xe

scp -rP 6022 src ubuntu@localhost:
ssh -p 6022 ubuntu@localhost chmod +x src/create-iso.sh
ssh -p 6022 ubuntu@localhost src/chroot-init.sh
ssh -p 6022 ubuntu@localhost src/chroot-clean.sh

