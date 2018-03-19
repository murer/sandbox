#!/bin/bash -xe

docker rm -f git-static-compile || true
docker run --rm -it --name git-static-compile git-static-compile
