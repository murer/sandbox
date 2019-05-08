#!/bin/bash -xe

basedir="$(dirname "$0")/../.."

cd "$basedir"

gsutil -m rsync -dc src/conf gs://sandbox-dataproc/src/conf

gcloud dataproc clusters create sandbox-dataproc \
    --project dxtserasa \
    --master-machine-type n1-standard-1 \
    --worker-machine-type n1-standard-1 \
    --zone us-east1-b \
    --bucket sandbox-dataproc \
    --num-workers 2 \
    --num-preemptible-workers 10 \
    --scopes cloud-platform \
    --initialization-actions gs://sandbox-dataproc/src/conf/init.sh


#--properties 'spark:spark.dynamicAllocation.enabled=false'
# --properties 'spark:spark.executor.memory=18gb,spark:spark.executor.cores=4' \
