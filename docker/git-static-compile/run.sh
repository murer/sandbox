#!/bin/bash -xe

rm -rf target || true
mkdir target

docker rm -f git-static-compile || true
docker run -it --name git-static-compile git-static-compile /bin/true
docker cp git-static-compile:/home/git/git-static.tar.gz target/git-static.tar.gz
docker rm -f git-static-compile || true
