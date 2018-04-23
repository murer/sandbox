#!/bin/bash -xe

docker run -ti --rm \
	-e DISPLAY \
	-v /tmp/.X11-unix:/tmp/.X11-unix \
	-v $HOME/.Xauthority:/home/obs/.Xauthority \
	--device /dev/dri \
	--net=host \
  obs
