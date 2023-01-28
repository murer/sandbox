#!/bin/bash -xe

function cmd_build() {
  mkdir -p target
  echo "(function() {" > target/roll21.js
  ls src | grep "\.js$" | sort | while read k; do
    cat "src/$k" >> target/roll21.js
  done
  echo "})()" >> target/roll21.js
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
