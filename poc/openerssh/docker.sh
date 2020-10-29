#!/bin/bash -xe

DOCKER_USER_ID="$(id -u):$(id -g)"

docker_golang() {
  docker volume create openerssh_golang_dev --label openerssh_dev 1>&2 || true
  docker run $OPENERSSH_DOCKER_EXTRA --rm --label openerssh_dev \
    --mount source=openerssh_golang_dev,target=/go \
    -v "$HOME/.config/gcloud:/go/.config/gcloud" \
    -v "$(pwd)":/go/src -w /go/src \
    -p 8080:8080 -e "HOME=/go" -u "$DOCKER_USER_ID" \
    golang:1.14 "$@"
}

docker_devvnc() {
  docker volume create openerssh_vscode_dev --label openerssh_dev 1>&2 || true
  docker run $OPENERSSH_DOCKER_EXTRA --rm --label openerssh_dev \
    --mount source=openerssh_vscode_dev,target=/home/hexblade/.vscode \
    -v "$(pwd)":/home/hexblade/openerssh \
    -p 5900:5900 \
    murer/openerssh-dev:local "$@"
}

docker_devx() {
  docker volume create openerssh_vscode_dev --label openerssh_dev 1>&2 || true
  docker run $OPENERSSH_DOCKER_EXTRA --rm --label openerssh_dev \
    --mount source=openerssh_vscode_dev,target=/home/hexblade/.vscode \
    -v "$(pwd)":/home/hexblade/openerssh \
    -e "DISPLAY=unix$DISPLAY" \
    -v "/tmp/.X11-unix:/tmp/.X11-unix" \
    murer/openerssh-dev:local "$@"
}

cmd_code() {
  cmd_rund devx code --verbose openerssh
}

cmd_cleanup() {
  docker ps -aq --filter label=openerssh_dev | xargs docker rm -f || true
  docker system prune --volumes --filter label=openerssh_dev -f || true
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

cmd_devimg() {
  docker build -t murer/openerssh-dev:local .
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
