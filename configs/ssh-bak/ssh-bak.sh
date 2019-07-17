#!/bin/bash -xe

ssh_bak_cmd=${1?"enc|dec"}
ssh_bak_file=${2?"tar.gz.crypt file"}

ssh_enc() {
  cd
  tar czvf - .ssh | openssl enc -aes-256-cbc -pbkdf2 -iter 100000 -salt -out "$ssh_bak_file"
  cd -
}

ssh_dec() {
  [[ ! -d ".ssh" ]]
  [[ ! -f ".ssh" ]]
  openssl enc -aes-256-cbc -d -pbkdf2 -iter 100000 -salt -in "$ssh_bak_file" | tar xvzf -
}

if   [ "x$ssh_bak_cmd" == "xenc" ]; then ssh_enc;
elif [ "x$ssh_bak_cmd" == "xdec" ]; then ssh_dec;
else false; fi
