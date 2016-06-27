#!/bin/bash -xe

docker rm -f mysql || true
docker run --rm -p 127.0.0.1:3306:3306 --name mysql mysql $*
