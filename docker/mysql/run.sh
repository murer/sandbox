#!/bin/bash -xe

docker rm -f mysql || true
docker run -d -p 127.0.0.1:3306:3306 -u mysql --name mysql mysql $*
