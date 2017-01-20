#!/bin/bash  -xe

docker rm -f nginxwebapp2-shell || true
docker run -it --rm -u root --name nginxwebapp2-shell nginxwebapp2 /bin/bash
