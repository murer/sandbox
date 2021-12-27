#!/bin/bash -xe

cmd_clean() {
    rm -rf target || true
}

cmd_test() {
    cmd_clean
    mkdir -p target/steps
    ./data.sh step1
    cp -R target/sandbox target/steps/step1
    ./tar.sh backup_step step1inc.tgz
    ./data.sh step2
    cp -R target/sandbox target/steps/step2
    ./tar.sh backup_step step2inc.tgz
    ./data.sh step3
    cp -R target/sandbox target/steps/step3
    ./tar.sh backup_step step3inc.tgz
    ./data.sh step4
    cp -R target/sandbox target/steps/step4
    ./tar.sh backup_step step4inc.tgz

    
}



cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
