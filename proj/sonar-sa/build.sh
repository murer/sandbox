#!/bin/bash -xe

cmd_build() {
    docker build -t murer/sonar-sa:dev .
}

cmd_server() {
    #docker run --name sonar --rm -it sonarqube:lts # -Dsonar.login=admin -Dsonar.password=admin
    true
}

cmd_shell() {
    docker run --name sonar_shell --rm -it murer/sonar-sa:dev /bin/bash
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"