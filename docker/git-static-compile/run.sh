#!/bin/bash -xe

rm -rf target || true
mkdir target

docker rm -f git-static-compile || true
docker run -it --name git-static-compile git-static-compile /bin/true
docker cp git-static-compile:/root/git.tar.gz target/git.tar.gz
docker rm -f git-static-compile || true
