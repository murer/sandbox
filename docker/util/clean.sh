#!/bin/bash

sudo docker rm $(sudo docker ps -a -q)
sudo docker images | grep none | cut -b40-56 | xargs sudo docker rmi 

sudo docker images

echo Use sudo docker rmi imagename

