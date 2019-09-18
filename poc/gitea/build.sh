#!/bin/bash -xe

workdir="$(
  cd "$(dirname "$0")/"
  pwd -P
)"

cmd_docker_build() {
  docker build -t 'dxtgitea:latest' docker
}

cmd_docker_run() {
  docker rm -f dxtgitea_shell || true
  docker run -it --rm --name dxtgitea_shell 'dxtgitea:latest' "$@"
}

cmd_docker_start() {
  docker rm -f dxtgitea_deamon || true
  docker run -it --rm --name dxtgitea_deamon \
    -p 3000:3000 \
    'dxtgitea:latest' "$@"
}

cmd_docker_exec() {
  docker exec -it dxtgitea_deamon "$@"
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -
