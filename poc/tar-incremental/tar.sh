#!/bin/bash -xe

cmd_backup_step() {
    output="${1?'output file'}"
    cd target
    mkdir -p bak
    tar cvgf data.sng "bak/$output" sandbox 
    cd -
    sleep 0.1
}

cmd_restore_step() {
    input="${1?'input file'}"
    cd target
    mkdir -p bak
    tar xvgf /dev/null "bak/$input" sandbox 
    cd -
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
