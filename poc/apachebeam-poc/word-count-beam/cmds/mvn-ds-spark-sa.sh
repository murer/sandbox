#!/bin/bash -xe

gsutil -m rm -r gs://mydataflow-poc/sample gs://mydataflow-poc/result gs://mydataflow-poc/tmp || true
gsutil -m cp -r sample gs://mydataflow-poc

mvn compile exec:java \
  -Pspark-runner \
  -Dexec.mainClass=org.apache.beam.examples.DSPOC \
  -Dexec.args="--runner=SparkRunner \
  --inputFile=sample/input.simple.csv \
  --output=target/poc/result"
