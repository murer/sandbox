#!/bin/bash -xe

gsutil -m rm -r gs://mydataflow-poc/sample gs://mydataflow-poc/result gs://mydataflow-poc/tmp || true
gsutil -m cp -r sample gs://mydataflow-poc

mvn compile exec:java \
  -Pdataflow-runner \
  -Dexec.mainClass=org.apache.beam.examples.DSPOC \
  -Dexec.args="--runner=DataflowRunner \
    --project=frotanetappdevel \
    --gcpTempLocation=gs://mydataflow-poc/tmp \
    --inputFile=gs://mydataflow-poc/sample/input.csv \
    --output=gs://mydataflow-poc/result/ret"
