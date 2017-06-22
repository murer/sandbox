#!/bin/bash -xe

docker rm -f hadoop || true
docker run -it --rm --name hadoop hadoop "$@"
