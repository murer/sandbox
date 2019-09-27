#!/bin/bash -xe

workdir="$(
  cd "$(dirname "$0")/"
  pwd -P
)"

cmd_cluster_create() {
  gcloud deployment-manager deployments create "couchdb1"
}

cmd_browser() {
  google-chrome \
    --user-data-dir="$workdir/gen/chrome-proxy" \
    --proxy-server="socks5://localhost:1080" \
    'http://cdb-vm:5984/_membership' \
    'http://cdb2-vm:5984/_membership' \
    'http://cdb3-vm:5984/_membership'
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
