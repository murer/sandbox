#!/bin/bash -xe

docker rm -f xtest|| true
docker run -it -p 5900:5900 --name xtest xtest "$@"
