#!/bin/bash -xe

rm -rf target/result || true
rm -rf target/result2 || true

./gen/spark/bin/spark-submit --master local[5] "$@"


set +x

cat target/result/part*

cat target/result/part* | wc

set -x
