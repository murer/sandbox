#!/bin/bash -xe

docker rm -f postgresql || true
docker run --rm -p 127.0.0.1:5432:5432 --name postgresql postgresql $*

