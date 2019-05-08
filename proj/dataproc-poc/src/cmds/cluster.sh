#!/bin/bash -xe

basedir="$(dirname "$0")/../.."

dataproc_sandbox_project=dxtserasa
dataproc_sandbox_zone=us-east1-b
dataproc_sandbox_bucket=sandbox-dataproc

cmd_cluster_delete() {
  gcloud dataproc clusters delete sandbox-dataproc -q \
      --project "$dataproc_sandbox_project" || true
}

cmd_cluster_create() {
  gsutil -m rsync -dc src/conf "gs://$dataproc_sandbox_bucket/src/conf"

  gcloud dataproc clusters create sandbox-dataproc \
      --project "$dataproc_sandbox_project" \
      --zone "$dataproc_sandbox_zone" \
      --master-machine-type n1-standard-1 \
      --worker-machine-type n1-standard-1 \
      --bucket "$dataproc_sandbox_bucket" \
      --num-workers 2 \
      --num-preemptible-workers 10 \
      --scopes cloud-platform \
      --optional-components=JUPYTER \
      --initialization-actions "gs://$dataproc_sandbox_bucket/src/conf/init.sh"
}

cmd_cluster_recreate() {
  cmd_cluster_delete
  cmd_cluster_create
}

cmd_ssh_master() {
  gcloud compute ssh sandbox-dataproc-m \
    --zone "$dataproc_sandbox_zone" \
    -- \
    -L 8085:localhost:8085
}

cd "$basedir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
