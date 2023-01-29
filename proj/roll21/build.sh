#!/bin/bash -xe

function cmd_build() {
  mkdir -p download
  echo "(function() {" > download/roll21.js
  ls src | grep "\.js$" | sort | while read k; do
    cat "src/$k" >> download/roll21.js
  done
  echo "})()" >> download/roll21.js
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
