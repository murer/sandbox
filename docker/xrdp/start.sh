#!/bin/bash -xe

docker rm -f xrdp || true
docker run -it --name xrdp xrdp "$@"
