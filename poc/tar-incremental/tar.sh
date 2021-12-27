#!/bin/bash -xe

cmd_backup_step() {
    output="${1?'output file'}"
    cd target
    mkdir -p bak
    tar cvpgf data.sng "bak/$output" sandbox 
    cd -
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
