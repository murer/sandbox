#!/bin/bash -xe

cmd_image() {
  docker build -t murer/pyrnet:latest .
}

cmd_run() {
  docker rm -f pyrnet || true
  docker run -it --rm --name pyrnet --privileged murer/pyrnet "$@"
}

cmd_start() {
  #cmd_run mitmproxy  --showhost --mode transparent
  cmd_run
}

cmd_shell() {
  docker exec -it pyrnet /bin/bash
}

cmd_local() {
  ~/.local/bin/mitmproxy --mode transparent --showhost --set "confdir=$(pwd)/conf"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
