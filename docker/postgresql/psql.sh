#!/bin/bash -xe

docker rm -f postgresql-client || true
docker run --rm --link postgresql:postgresql -it --name postgresql-client postgresql /opt/config/psql.sh $*

