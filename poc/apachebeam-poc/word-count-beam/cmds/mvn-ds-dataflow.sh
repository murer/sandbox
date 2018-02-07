#!/bin/bash -xe

gsutil -m rm -r gs://mydataflow-poc/result

mvn compile exec:java \
  -Pdataflow-runner \
  -Dexec.mainClass=org.apache.beam.examples.DSPOC \
  -Dexec.args="--runner=DataflowRunner \
    --project=frotanetappdevel \
    --gcpTempLocation=gs://mydataflow-poc/tmp \
    --inputFile=gs://mydataflow-poc/sample/input.simple.csv \
    --output=gs://mydataflow-poc/result"
