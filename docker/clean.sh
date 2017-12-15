#!/bin/bash -xe

while docker ps -aq | grep '.\+'; do
  docker ps -aq | xargs docker rm -f
done

while docker images -aq | grep '.\+'; do
  docker images -aq | xargs docker rmi
done
