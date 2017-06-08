#!/bin/bash  -xe

docker rm -f ruby-shell || true
docker run -it --rm -u root --name ruby-shell ruby:2.4 /bin/bash
