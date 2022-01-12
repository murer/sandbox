#!/bin/bash -xe

cmd_build() {
  docker build -t tesourodireto/tesourodireto:dev .
}

cmd_clean() {
  docker ps -aq --filter label=tesourodireto_dev | xargs docker rm -f || true
  docker system prune --volumes --filter label=tesourodireto_dev -f || true
}

cmd_run() {
  docker run -it --rm --label tesourodireto_dev \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    tesourodireto/tesourodireto:dev "$@"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
