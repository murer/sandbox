#!/bin/bash -xe

cmd_image() {
  docker build -t murer/ssh-mitm:latest .
}

cmd_run() {
  docker rm -f ssh-mitm || true
  docker run -it --rm \
    --name ssh-mitm \
    -u ssh-mitm \
    murer/ssh-mitm:latest "$@"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
