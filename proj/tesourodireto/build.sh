#!/bin/bash -xe

cmd_build() {
  docker build -t tesourodireto/tesourodireto:dev .
}

cmd_clean() {
  docker ps -aq --filter label=tesourodireto_dev | xargs docker rm -f || true
  docker system prune --volumes --filter label=tesourodireto_dev -f || true
}

cmd_run() {
  docker run -it --rm --label tesourodireto_dev --name tesourodireto_dev \
    -v "$(pwd)/src:/home/hexblade/tesourodireto" \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    -p 5900:5900 -p 127.0.0.1:8888:8888 \
    tesourodireto/tesourodireto:dev "$@"
}

cmd_run_own() {
  docker run -it --rm --label tesourodireto_dev --name tesourodireto_dev \
    -v "$(pwd)/src:/home/hexblade/tesourodireto" \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    -e "DISPLAY=:0" \
    -p 127.0.0.1:8888:8888 \
    tesourodireto/tesourodireto:dev "$@" # python3 tesourodireto/sample_test.py
}

cmd_run_jupyter() {
  cmd_run_own /home/hexblade/.local/bin/jupyter notebook --ip=0.0.0.0
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
