#!/bin/bash  -xe

docker rm -f ubuntu-shell || true
docker run -it --rm -u root --name ubuntu-shell ubuntu:14.04 /bin/bash
