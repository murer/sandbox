#!/bin/bash  -xe

docker rm -f postgresql-shell || true
docker run -it --rm -u root --name postgresql-shell postgresql /bin/bash
