#!/bin/bash -xe

export DEBIAN_FRONTEND="noninteractive"

cmd_enc() {
  [[ -f repoz.tar.gz ]]
  rm repoz.tar.gz.gpg || true
  gpg --no-options --batch -c --compress-algo none --passphrase-file "$HOME/.ssh/id_rsa" -o repoz.tar.gz.gpg repoz.tar.gz
}

cmd_dec() {
  [[ -f repoz.tar.gz.gpg ]]
  rm repoz.tar.gz || true
  gpg --no-options --batch -d --compress-algo none --passphrase-file "$HOME/.ssh/id_rsa" -o repoz.tar.gz repoz.tar.gz.gpg
}


cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
