#!/bin/bash  -xe

docker rm -f apachewebapp2-server || true
docker run -it --rm -p 8000:80 -u root --name apachewebapp2-server apachewebapp2
