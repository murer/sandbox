#!/bin/bash -xe

service ssh start || true

hdfs namenode -format

hadoop-daemon.sh --script hdfs start namenode
hadoop-daemon.sh --script hdfs start datanode

hadoop fs -mkdir /user
hadoop fs -mkdir /user/root

echo abc | hadoop fs -put - /user/root/x.txt
dd if=/dev/zero count=10240 bs=1024 | hadoop fs -put - /user/root/large.txt

#start-dfs.sh
