#!/bin/bash  -xe

docker rm -f nginxwebapp2-server || true
docker run -it --rm -p 8000:80 -u root --name nginxwebapp2-server nginxwebapp2
