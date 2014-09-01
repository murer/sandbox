#!/bin/bash -e

sudo docker images -qa | xargs sudo docker rmi -f

