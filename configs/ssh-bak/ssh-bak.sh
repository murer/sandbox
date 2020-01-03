#!/bin/bash -xe

cd "$(dirname "$0")"
basedir="$(pwd)"
cd -

cmd_enc() {
  cd
  tar czvf - .ssh | openssl enc -aes-256-cbc -pbkdf2 -iter 100000 -salt -out "$basedir/ssh.keys.tar.gz.crypt"
  cd -
}

cmd_dec() {
  cd
  [[ ! -d ".ssh" ]]
  [[ ! -f ".ssh" ]]
  openssl enc -aes-256-cbc -d -pbkdf2 -iter 100000 -salt -in "$basedir/ssh.keys.tar.gz.crypt" | tar xvzf -
  cd -
}

_cmd="${1?'cmd is required'}"
shift
cmd_${_cmd} "$@"
