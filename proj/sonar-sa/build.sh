#!/bin/bash -xe

cmd_build() {
    docker build -t murer/sonar-sa:dev .
}

cmd_sa() {
    #docker run --name sonar --rm -it sonarqube:lts # -Dsonar.login=admin -Dsonar.password=admin
    docker run --name sonar-sa --rm -p 9000:9000 -it murer/sonar-sa:dev entrypoint/sonar-sa.sh "$@"
}

cmd_shell() {
    docker run --name sonar_shell --rm -it murer/sonar-sa:dev /bin/bash
}

cmd_exec() {
    docker exec -it sonar-sa /bin/bash
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"