#!/bin/bash -xe

mvn compile exec:java \
  -Pdirect-runner \
  -Dexec.mainClass=org.apache.beam.examples.WordCount \
  -Dexec.args=" \
    --inputFile=pom.xml \
    --output=target/sample/result"
