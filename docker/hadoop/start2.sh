#!/bin/bash -xe

docker rm -f hadoop2 || true
docker run -it --rm --name hadoop2 hadoop "$@"
