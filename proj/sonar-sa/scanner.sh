#!/bin/bash -xe

docker run \
    --rm \
    -e SONAR_HOST_URL="http://172.17.0.2:9000/" \
    -e SONAR_LOGIN="b9a060a1077eb81f637e9747553bb877461a0fff" \
    -v "$(pwd):/usr/src" \
    sonarsource/sonar-scanner-cli:latest \
    -Dsonar.projectKey=abc