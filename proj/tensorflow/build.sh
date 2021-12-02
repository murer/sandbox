#!/bin/bash -xe

cmd_prepare() {
    sudo apt install python3-pip
    pip install jupyterlab
}

cmd_start() {
    jupyter notebook
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"