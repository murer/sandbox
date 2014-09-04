#!/bin/bash -e

sudo docker images -q --no-trunc | xargs sudo docker rmi

