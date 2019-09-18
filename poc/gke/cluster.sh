#!/bin/bash -xe

workdir="$(
  cd "$(dirname "$0")/"
  pwd -P
)"

cmd_prepare_local() {
  gcloud components install kubectl -q
  gcloud container clusters get-credentials testcluster \
    --project dxtgitlab \
    --zone us-east1-b
  kubectl config current-context
  kubectl config view
}

cmd_cluster_delete() {
  gcloud container clusters delete testcluster -q \
    --project dxtgitlab \
    --zone us-east1-b
}

cmd_cluster_recreate() {
  cmd_cluster_delete || true
  gcloud container clusters create testcluster \
    --project dxtgitlab \
    --zone us-east1-b \
    --enable-stackdriver-kubernetes
  cmd_prepare_local
}

cmd_app_deploy() {
  kubectl apply -f "${1?"app is required: app/hello-app"}"
}

cmd_app_delete() {
  app_name="${1?"app name is required"}"
  kubectl delete deployment "$app_name"
}

cmd_app_desc() {
  app_name="${1?"app name is required"}"
  # kubectl get pods # -L key=value
  kubectl describe deployment "$app_name"
  kubectl get service "$app_name"
}

cmd_pod_list() {
  kubectl get pods
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
