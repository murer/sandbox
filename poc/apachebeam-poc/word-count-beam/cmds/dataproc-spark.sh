#!/bin/bash -xe

mvn package -Pspark-runner

gsutil -m rm -r gs://mydataflow-poc/sample gs://mydataflow-poc/result gs://mydataflow-poc/tmp || true
gsutil -m cp -r sample gs://mydataflow-poc

#gsutil cp \
#  /home/murer/proj/sandbox/poc/apachebeam-poc/word-count-beam/target/word-count-beam-bundled-0.1.jar \
#  gs://mydataflow-poc/sample/test.jar

gcloud dataproc jobs submit spark \
  --region us-east1 \
  --cluster mycluster \
  --jars=target/word-count-beam-bundled-0.1.jar \
  --class=org.apache.beam.examples.DSPOC \
  -- \
  --runner=SparkRunner \
  --inputFile=gs://mydataflow-poc/sample/input.csv \
  --output=gs://mydataflow-poc/result/ret
