#!/bin/bash -xe

cmd_step1() {
    rm -rf target/sandbox || true
    rm -rf target/steps || true
    mkdir -p target/sandbox target/steps
}

cmd_step2() {
    echo a > target/sandbox/a.txt
    echo b > target/sandbox/b.txt
    echo c > target/sandbox/c.txt
    echo d > target/sandbox/d.txt
    echo e > target/sandbox/e.txt
    mkdir target/sandbox/x target/sandbox/y target/sandbox/z
    echo y > target/sandbox/y/y.txt
    echo z > target/sandbox/z/z.txt
}

cmd_step3() {
    echo m > target/sandbox/m.txt
    echo b2 > target/sandbox/b.txt
    rm target/sandbox/c.txt
    mv target/sandbox/d.txt target/sandbox/d2.txt
    mv target/sandbox/e.txt target/sandbox/x
    rm target/sandbox/y/y.txt
    rm -rf target/sandbox/z
}

cmd_step4() {
    rm -rf target/sandbox
    mkdir -p target/sandbox
    echo done > target/sandbox/done.txt
}


cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
