#!/bin/bash -xe

[[ "$UID" == "0" ]]

./src/build/build.sh drop || true
rm -rf target
