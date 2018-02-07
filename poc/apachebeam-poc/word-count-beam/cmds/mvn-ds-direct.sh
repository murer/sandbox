#!/bin/bash -xe

mvn compile exec:java \
  -Pdirect-runner \
  -Dexec.mainClass=org.apache.beam.examples.DSPOC \
  -Dexec.args=" \
    --inputFile=sample/input.simple.csv \
    --output=target/poc/result"
