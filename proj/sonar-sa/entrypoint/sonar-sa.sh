#!/bin/bash

set -xeuo pipefail

cmd_server_start() {
    cd /opt/sonarqube
    ./bin/linux-x86-64/sonar.sh console &
    cd -
}

cmd_server_wait_for_start() {
    set +x
    while ! curl -s 'http://localhost:9000/api/system/status' | jq .status | grep '^"UP"$'; do
        echo "NOT STARTED.... WAITING"
        sleep 1
    done
    set -x

    curl -f -v -u admin:admin 'http://localhost:9000/api/users/change_password' \
        --data-urlencode "login=admin" \
        --data-urlencode "password=123" \
        --data-urlencode "previousPassword=admin"

}

cmd_server_project_create() {
    curl -f -v -u admin:123 'http://localhost:9000/api/projects/create' \
        --data-urlencode "name=local" \
        --data-urlencode "project=local" \
        --data-urlencode "visibility=private"
}

cmd_server_token_generate() {
    curl -f -v -u admin:123 'http://localhost:9000/api/user_tokens/generate' \
        --data-urlencode "login=admin" \
        --data-urlencode "name=token" | jq -r .token > /tmp/sonarqa.token
}

cmd_scan() {
    /opt/sonar-scanner/bin/sonar-scanner \
        -Dsonar.projectKey=local \
        -Dsonar.sources=/opt/sonar-sa/src \
        -Dsonar.host.url=http://localhost:9000 \
        "-Dsonar.login=$(cat /tmp/sonarqa.token)"
}

cmd_main() {
    cmd_server_start
    cmd_server_wait_for_start
    cmd_server_project_create
    cmd_server_token_generate

    tail -f /dev/null
}

cd "$(dirname "$0")/.."; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"