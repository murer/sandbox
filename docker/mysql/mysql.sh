#!/bin/bash -xe

docker rm -f mysql-client || true
docker run --rm --link mysql:mysql -it --name mysql-client mysql /opt/config/mysql.sh
