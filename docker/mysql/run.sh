#!/bin/bash -xe

docker rm -f mysql || true
docker run --rm -p 127.0.0.1:5432:5432 --name mysql mysql $*
