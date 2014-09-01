#!/bin/bash -xe

sudo docker images -a  | grep ubuntu -m 1 -B 100 | tail -n +1 | head -n -1 | sed "s/\s\+/\t/g"| cut -f3 | xargs sudo docker rmi

