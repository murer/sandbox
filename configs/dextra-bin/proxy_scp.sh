#!/bin/bash

scp -o "StrictHostKeyChecking=no" -o ProxyCommand="connect -H $USER@172.16.120.1:3128 %h %p" $*

