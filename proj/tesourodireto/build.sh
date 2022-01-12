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
    -v "$(pwd)/src:/home/hexblade/tesourodireto" \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    tesourodireto/tesourodireto:dev "$@"
}

cmd_run_own() {
  docker run -it --rm --label tesourodireto_dev \
    -v "$(pwd)/src:/home/hexblade/tesourodireto" \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    -e "DISPLAY=:0" \
    tesourodireto/tesourodireto:dev python3 tesourodireto/sample_test.py
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
