#!/bin/bash

set -xeuo pipefail

cmd_server() {
    ./bin/linux-x86-64/sonar.sh start
}

cmd_tail_logs() {
    tail -f logs/* &
}

cmd_wait_for_start() {
    while ! curl 'http://localhost:9000/api/system/status' | jq .status | grep '^"UP"$'; do
        tail logs/*
        echo "NOT STARTED.... WAITING"
        sleep 1
    done
}

cmd_change_default_password() {
    # rm /tmp/sonar-sa.api.cookies || true
    
    # curl -f -v -c /tmp/sonar-sa.api.cookies 'http://localhost:9000/api/authentication/login' \
    #     --data-urlencode "login=admin" \
    #     --data-urlencode "password=admin"

    curl -f -v -u admin:admin 'http://localhost:9000/api/users/change_password' \
        --data-urlencode "login=admin" \
        --data-urlencode "password=123" \
        --data-urlencode "previousPassword=admin"
}

cmd_main() {
    cmd_server
    
    #sleep 2
    #cmd_tail_logs

    cmd_wait_for_start
    cmd_change_default_password

    tail -f logs/*


}

cd "$(dirname "$0")/.."; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"