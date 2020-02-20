#!/bin/bash -xe

cmd_build() {
  docker build -t murer/pyrnet:latest .
}

cmd_run() {
  docker rm -f pyrnet || true
  docker run -it --rm -p 8080:8080 --name pyrnet murer/pyrnet "$@"
}

cmd_start() {
  cmd_run mitmproxy  --showhost # --mode transparent
}

cmd_shell() {
  docker exec -it pyrnet /bin/bash
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
