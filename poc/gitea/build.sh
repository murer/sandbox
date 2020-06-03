#!/bin/bash -xe

cmd_cleanup() {
  docker ps -aq --filter label=dsavpn_dev | xargs docker rm -f || true
  docker system prune --volumes --filter label=dsavpn_dev -f || true
}

cmd_docker_build() {
  docker build -t 'dsasource/gitea:local' .
}

cmd_docker_run() {
  docker rm -f dsasource_gitea || true
  docker run -it --rm --name dsasource_gitea 'dsasource/gitea:local' "$@"
}

cmd_docker_start() {
  docker volume create dsasource_gitea_data --label dsavpn_dev 1>&2 || true
  docker rm -f dsasource_gitea || true
  docker run -it --rm --name dsasource_gitea --label dsasource_dev \
    --mount source=dsasource_gitea_data,target=/var/lib/gitea \
    -p 3000:3000 \
    'dsasource/gitea:local' "$@"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
