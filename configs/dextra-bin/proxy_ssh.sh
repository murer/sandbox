#!/bin/bash

ssh -o "UserKnownHostsFile=/dev/null" -o "StrictHostKeyChecking=no" -o ProxyCommand="connect -H $USER@172.16.120.1:3128 %h %p" $*

