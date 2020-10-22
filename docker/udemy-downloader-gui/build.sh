#!/bin/bash -xe

cmd_clean() {
  docker rm -f udemy-downloader-gui || true
}

cmd_build() {
  docker build -t murer/udemy-downloader-gui:latest .
}

cmd_run() {
  docker run --name udemy-downloader-gui -p 5900:5900 murer/udemy-downloader-gui:latest
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
