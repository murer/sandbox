#!/bin/bash -xe

(/usr/lib/postgresql/9.3/bin/postgres -D /var/lib/postgresql/9.3/main -c config_file=/etc/postgresql/9.3/main/postgresql.conf > /tmp/postgesql.log 2>&1 &)

sleep 2

#export PGPASSWORD=docker

#psql -h localhost -U docker docker

