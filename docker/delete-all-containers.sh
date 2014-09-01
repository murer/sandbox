#!/bin/bash -e

sudo docker ps -aq | xargs sudo docker rm -f

