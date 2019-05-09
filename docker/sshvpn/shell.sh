#!/bin/bash  -xe

docker rm -f sshvpn-shell || true
docker run -it --rm --name sshvpn-shell \
  -v "$HOME/.ssh:/root/.ssh" \
  murer/sshvpn:latest /bin/bash
