#!/bin/bash  -xe

docker rm -f apachewebapp2-shell || true
docker run -it --rm -u root --name apachewebapp2-shell apachewebapp2 /bin/bash
