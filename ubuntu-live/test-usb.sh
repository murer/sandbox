#!/bin/bash -xe

scp -rP 6022 src ubuntu@localhost:
ssh -p 6022 ubuntu@localhost src/create-usb.sh "$1"

