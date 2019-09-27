#!/bin/bash -xe

workdir="$(
  cd "$(dirname "$0")/"
  pwd -P
)"

cmd_docker_build() {
  docker build -t 'murer/couchdb:latest' docker
}

cmd_docker_network() {
  docker network rm couchdb || true
  docker network create couchdb
}

cmd_docker_run() {
  db_name="${1?'db_name'}"
  docker run \
    -it --rm --name "$db_name" \
    --network couchdb \
    -h "$db_name" \
    -e "NODENAME=$db_name" \
    -e "COUCHDB_USER=admin" \
    -e "COUCHDB_PASSWORD=123" \
    -e "COUCHDB_SECRET=123" \
    'murer/couchdb:latest'
}

cmd_docker_exec() {
  docker exec -it db1 /bin/bash
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
