#!/bin/bash -xe

echo You must 'gcloud auth application-default login'

mvn package -Pdataflow-runner

java -cp ./target/word-count-beam-bundled-0.1.jar \
  org.apache.beam.examples.WordCount \
  --runner=DataflowRunner \
  --project=frotanetappdevel \
  --gcpTempLocation=gs://mydataflow-poc/tmp \
  --inputFile=gs://frotanetappdevel.appspot.com/db/secret/* \
  --output=gs://mydataflow-poc/result
