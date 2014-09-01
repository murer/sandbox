#!/bin/bash -e

sudo docker images | tail -n +2 | sed "s/\s\+/\t/g" | cut -f3 | xargs sudo docker rmi -f

