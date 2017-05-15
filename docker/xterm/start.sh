#!/bin/bash -xe

docker rm -f xterm || true
docker run -it -v "/tmp/.X11-unix:/tmp/.X11-unix" -e "DISPLAY=$DISPLAY" --name xterm xterm
