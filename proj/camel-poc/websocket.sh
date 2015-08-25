#!/bin/bash -xe

#export TARGET="echo.websocket.org"
export TARGET="localhost:5006"

curl -i -N -H "Sec-WebSocket-Version: 13" -H "Connection: Upgrade" -H "Upgrade: websocket" -H "Host: $TARGET" -H "Origin: http://$TARGET" "http://$TARGET"

