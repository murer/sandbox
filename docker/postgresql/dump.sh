#!/bin/bash -xe

if [ "x$1" == "x" ]; then
	echo "Usage:";
	echo "  $0 <host> <port> <user> <password> <database> [-n <schema>, -n <schema>, ...]";
	exit 1;
fi

PG_HOST=$1
PG_PORT=$2
PG_USER=$3
PG_PASSWORD=$4
PG_DATABASE=$5
shift 5

export PGPASSWORD="$PG_PASSWORD"
pg_dump -v -O -x -h $PG_HOST -p $PG_PORT -U $PG_USER $* $PG_DATABASE
