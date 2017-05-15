#!/bin/bash -xe

docker rm -f xtest|| true
docker run -it --name xtest xtest
