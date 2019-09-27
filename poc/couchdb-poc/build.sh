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
  docker network createb
}

cmd_docker_run() {
  db_name="${1?'db_name'}"
  docker run \
    -it --rm --name "$db_name" \
    --network couchdb \
    -h "$db_name" \
    -e "db_name=$db_name" \
    'murer/couchdb:latest'
}
# -e 'ERL_FLAGS=-setcookie monster -name couchdb@db1.couchdb' \
# -e "COUCHDB_USER=admin" \
# -e "COUCHDB_PASSWORD=123" \
# -e "COUCHDB_SECRET=monster" \
#    -e 'ERL_FLAGS=-setcookie "monster"' \
#     -e "NODENAME=${db_name}.couchdb" \
#    -e "COUCHDB_SECRET=monster" \


cmd_docker_exec() {
  db_name="${1?'db_name'}"
  docker exec -it "$db_name" /bin/bash
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
