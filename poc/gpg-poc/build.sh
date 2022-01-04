#!/bin/bash -xe

cmd_clean() {
    rm -rf target || true
}

cmd_data() {
    mkdir -p target/files
    echo a > target/files/a.txt
    echo a > target/files/b.txt
    mkdir target/files/x
    echo c > target/files/x/c.txt
}

cmd_test() {
    cmd_clean
    cmd_data
    echo SUCCESS
}



cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
