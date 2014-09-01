#!/bin/bash -e

sudo docker images -a | tail -n +2 | sed "s/\s\+/\t/g" | cut -f3 | xargs sudo docker rmi -f

