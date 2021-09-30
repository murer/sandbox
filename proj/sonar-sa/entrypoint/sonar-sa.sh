#!/bin/bash

set -xeuo pipefail

cmd_server() {
    ./bin/linux-x86-64/sonar.sh console
}

cmd_wait_for_start() {
    while ! curl 'http://localhost:9000/api/system/status' | jq .status | grep '^"UP"$'; do
        #tail logs/*
        echo "NOT STARTED.... WAITING"
        sleep 1
    done

    curl -f -v -u admin:admin 'http://localhost:9000/api/users/change_password' \
        --data-urlencode "login=admin" \
        --data-urlencode "password=123" \
        --data-urlencode "previousPassword=admin"

}

# cmd_xabsdksda() {
    # rm /tmp/sonar-sa.api.cookies || true
    
    # curl -f -v -c /tmp/sonar-sa.api.cookies 'http://localhost:9000/api/authentication/login' \
    #     --data-urlencode "login=admin" \
    #     --data-urlencode "password=admin"
# }

cmd_main() {
    cmd_server &
    cmd_wait_for_start 1> logs/sonar-sa-wait-for-start.log 2>&1
    tail -f /dev/null
}

cd "$(dirname "$0")/.."; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"