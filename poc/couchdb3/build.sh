#!/bin/bash -xe

function cmd_clean() {
  docker ps -aq --filter label=couchdb-poc | xargs docker rm -f || true
  docker system prune --volumes --filter label=couchdb-poc -f || true
}


function cmd_run() {
  docker volume create --label couchdb-poc couchdb-poc
	docker run --rm -it --label couchdb-poc --name couchdb-poc \
    --mount 'src=couchdb-poc,dst=/opt/couchdb/data' \
    -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=123 \
	  apache/couchdb:3	
}

set +x; cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; set -x; "cmd_${_cmd}" "$@"
