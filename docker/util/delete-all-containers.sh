#!/bin/bash -e

sudo docker ps -aq --no-trunc | xargs sudo docker rm -f

