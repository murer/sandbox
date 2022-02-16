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
    -p 5984:5984 \
	  apache/couchdb:3	
}

function cmd_view_update_map() {
  curl -vX PUT http://admin:123@127.0.0.1:5984/mydb/_design/my_test \
     -d '{"views":{"my_filter":{"map":
         "function(doc) { if(doc.name) { emit(doc.name, doc); }}"}}}'
}

function cmd_view_update_reduce() {
  curl -vX PUT http://admin:123@127.0.0.1:5984/mydb/_design/my_reduce \
     -d '{
        "views": { 
          "my_filter": {
            "map": "function(doc) { if(doc.name) { emit(doc.name, doc); }}",
            "reduce": "function() { return 1; }"
          }
        }
      }'
}

set +x; cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; set -x; "cmd_${_cmd}" "$@"
