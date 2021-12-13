#!/bin/bash -xe

cmd_prepare() {
    sudo apt install python3-pip
    pip install jupyterlab
    pip install matplotlib
    pip install pandas

    rm -rf target/install || true
    mkdir -p target/install
    cd target/install
    wget https://storage.googleapis.com/tensorflow/linux/cpu/tensorflow_cpu-2.7.0-cp38-cp38-manylinux2010_x86_64.whl
    pip install tensorflow_cpu-2.7.0-cp38-cp38-manylinux2010_x86_64.whl
    cd -
}

cmd_start() {
    jupyter notebook
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"