#!/bin/bash -xe

export PGPASSWORD=docker

psql -h "$POSTGRESQL_PORT_5432_TCP_ADDR" -p "$POSTGRESQL_PORT_5432_TCP_PORT" -U docker
