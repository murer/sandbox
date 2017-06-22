#!/bin/bash -xe

service ssh start || true

hadoop-daemon.sh --script hdfs start datanode
