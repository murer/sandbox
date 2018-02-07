#!/bin/bash -xe

mvn package -Pspark-runner

gsutil cp \
  /home/murer/proj/sandbox/poc/apachebeam-poc/word-count-beam/target/word-count-beam-bundled-0.1.jar \
  gs://mydataflow-poc/sample/test.jar
