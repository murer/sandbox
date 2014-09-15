#!/bin/bash

scp -o "StrictHostKeyChecking=no" -o ProxyCommand="connect -H $USER@172.16.129.4:3128 %h %p" $*

