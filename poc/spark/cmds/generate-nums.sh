#!/bin/bash -xe

rm -rf gen/data/nums || true
mkdir -p gen/data/nums

seq "$(( $1 * $2 ))" | split -l "$2" -da 8 - gen/data/nums/n-
