#!/bin/bash -xe

rm -rf target || true
mkdir target

docker rm -f git-static-compile || true
docker run -it --name git-static-compile git-static-compile
