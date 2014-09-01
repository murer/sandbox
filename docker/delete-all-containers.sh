#!/bin/bash -e

sudo docker ps -a | tail -n +2 | cut -d" " -f1 | xargs sudo docker rm -f

