#!/bin/bash  -xe

docker rm -f mysql-shell || true
docker run -it --rm --link mysql:mysql -u root --name mysql-shell mysql /bin/bash
