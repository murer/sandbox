#!/bin/bash -xe

cmd_clean() {
  docker rm -f jdownaloder2 || true
}

cmd_build() {
  docker build -t murer/jdownloader2:latest .
}

cmd_run() {
  docker run --name jdownaloder2 -p 5900:5900 murer/jdownloader2:latest
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
