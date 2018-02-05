#!/bin/bash -xe

echo You must 'gcloud auth application-default login'

mvn package -Pdirect-runner

java -cp ./target/word-count-beam-bundled-0.1.jar \
  org.apache.beam.examples.WordCount \
  --inputFile=pom.xml \
  --output=target/sample/result
