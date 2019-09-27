#!/bin/bash -xe

workdir="$(
  cd "$(dirname "$0")/"
  pwd -P
)"

gcp_project=dxtdna
gcp_zone=us-east1-b

cmd_docker_build() {
  docker build -t 'gcr.io/dxtdna/couchdb:latest' docker
}

cmd_docker_push() {
  docker push 'gcr.io/dxtdna/couchdb:latest'
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
    -e "db_name=$db_name" \
    -e "db_dns=couchdb" \
    'gcr.io/dxtdna/couchdb:latest'
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

cmd_cluster_prepare() {
  gcloud compute
}

cmd_cluster_delete() {
  gcloud compute instances delete -q couchdb1 couchdb2 \
    --project "$gcp_project" --zone "$gcp_zone"
}

cmd_cluster_create() {
  gcloud compute instances create couchdb1 couchdb2 \
    --project "$gcp_project" --zone "$gcp_zone" \
    --source-instance-template=couchdb-v2 \
    --metadata-from-file 'startup-script=gcp/gcp-entrypoint.sh'
}

cmd_cluster_recreate() {
  cmd_cluster_delete || true
  cmd_cluster_create
}

cmd_cluster_proxy() {
  gcloud compute ssh couchdb1 \
    --project "$gcp_project" --zone "$gcp_zone" \
    -- -D 1080
}

cmd_cluster_browser() {
  (google-chrome \
    --user-data-dir="$workdir/gen/chrome-proxy" \
    --proxy-server="socks5://localhost:1080" \
    'http://couchdb1:5984/_utils' \
    'http://couchdb2:5984/_utils' \
    > /dev/null 2>&1 &)
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
