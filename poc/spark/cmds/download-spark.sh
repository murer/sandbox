#!/bin/bash -xe

if ! gen/spark/bin/spark-submit --version; then
  rm -rf gen/spark gen/spark-2.2.0-bin-hadoop2.6 gen/spark.tgz || true
  wget https://repoz.dextra.com.br/repoz/r/pub/spark/spark-2.2.0-bin-hadoop2.6.tgz \
    -O gen/spark.tgz
  cd gen
  tar xzf spark.tgz
  mv spark-2.2.0-bin-hadoop2.6 spark
  sed -e 's/rootCategory=INFO/rootCategory=WARN/g' \
    spark/conf/log4j.properties.template > spark/conf/log4j.properties
  cd -
fi
