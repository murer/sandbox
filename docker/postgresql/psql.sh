#!/bin/bash -xe

docker run --rm --link postgresql:postgresql -t --name postgresql-client postgresql /opt/config/psql.sh

