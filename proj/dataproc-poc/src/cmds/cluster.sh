#!/bin/bash -xe

basedir="$(dirname "$0")/../.."

dataproc_sandbox_cluster_name=sandbox-dataproc
dataproc_sandbox_project=dxtserasa
dataproc_sandbox_zone=us-east1-b
dataproc_sandbox_bucket="$dataproc_sandbox_cluster_name"

cmd_cluster_delete() {
  gcloud dataproc clusters delete "$dataproc_sandbox_cluster_name" -q \
      --project "$dataproc_sandbox_project" || true
}

cmd_cluster_create() {
  gsutil -m rsync -dc src/conf "gs://$dataproc_sandbox_bucket/src/conf"

  gcloud beta dataproc clusters create "$dataproc_sandbox_cluster_name" \
      --project "$dataproc_sandbox_project" \
      --zone "$dataproc_sandbox_zone" \
      --master-machine-type n1-standard-1 \
      --worker-machine-type n1-standard-1 \
      --bucket "$dataproc_sandbox_bucket" \
      --num-workers 2 \
      --num-preemptible-workers 3 \
      --scopes cloud-platform \
      --enable-component-gateway \
      --optional-components=ANACONDA,JUPYTER \
      --properties 'spark:spark.ui.killEnabled=true' \
      --initialization-actions "gs://$dataproc_sandbox_bucket/src/conf/init.sh"
}

cmd_cluster_recreate() {
  cmd_cluster_delete
  cmd_cluster_create
}

cmd_ssh_master() {
  gcloud compute ssh "$dataproc_sandbox_cluster_name-m" \
    --zone "$dataproc_sandbox_zone"
}

cd "$basedir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
