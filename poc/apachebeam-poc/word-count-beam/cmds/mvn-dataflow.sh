#!/bin/bash -xe

echo You must 'gcloud auth application-default login'

mvn compile exec:java \
  -Pdataflow-runner \
  -Dexec.mainClass=org.apache.beam.examples.WordCount \
  -Dexec.args="--runner=DataflowRunner \
    --project=frotanetappdevel \
    --gcpTempLocation=gs://mydataflow-poc/tmp \
    --inputFile=gs://apache-beam-samples/shakespeare/* \
    --output=gs://mydataflow-poc/result"
