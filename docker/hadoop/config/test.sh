#!/bin/bash -xe

service ssh start || true

hdfs namenode -format

start-dfs.sh
