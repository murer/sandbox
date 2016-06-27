#!/bin/bash  -xe

docker rm -f mysql-shell || true
docker run -it --rm -u root --name mysql-shell mysql /bin/bash
