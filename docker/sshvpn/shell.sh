#!/bin/bash  -xe

docker rm -f sshvpn-shell || true
docker run -it --rm --name sshvpn-shell \
  --privileged \
  murer/sshvpn:latest /bin/bash

# sudo sshuttle -r murer@fuweweu.com 0/0 -vvvvv
