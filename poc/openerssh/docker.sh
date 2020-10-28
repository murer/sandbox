#!/bin/bash -xe

DOCKER_USER_ID="$(id -u):$(id -g)"

docker_golang() {
  docker volume create openerssh_golang_dev --label openerssh_golang_dev 1>&2 || true
  docker run $OPENERSSH_DOCKER_EXTRA --rm --label openerssh_golang_dev \
    --mount source=openerssh_golang_dev,target=/go \
    -v "$HOME/.config/gcloud:/go/.config/gcloud" \
    -v "$(pwd)":/go/src -w /go/src \
    -p 8080:8080 -e "HOME=/go" -u "$DOCKER_USER_ID" \
    golang:1.14 "$@"
}

cmd_cleanup() {
  docker ps -aq --filter label=openerssh_golang_dev | xargs docker rm -f || true
  docker system prune --volumes --filter label=openerssh_golang_dev -f || true
}

cmd_run() {
  dockername="${1?'docker name'}"
  shift
  "docker_${dockername}" "$@"
}

cmd_runi() {
  istty=-i
  [[ -t 0 ]] && istty=-it
  OPENERSSH_DOCKER_EXTRA="$OPENERSSH_DOCKER_EXTRA $istty" cmd_run "$@"
}

cmd_rund() {
  OPENERSSH_DOCKER_EXTRA="$OPENERSSH_DOCKER_EXTRA -dit" cmd_run "$@"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
