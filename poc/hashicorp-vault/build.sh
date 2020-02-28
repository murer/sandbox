#!/bin/bash -xe

cmd_image() {
  docker build -t murer/hashicorp-vault:latest .
}

cmd_run() {
  docker rm -f pyrnet || true
  docker run -it --rm --name hashicorp-vault \
    --privileged \
    --network host \
    --volume /var/run/dbus:/var/run/dbus \
    murer/hashicorp-vault "$@"
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
