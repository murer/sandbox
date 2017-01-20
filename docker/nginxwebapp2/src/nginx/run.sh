#!/bin/bash -xe

#nginx -g 'daemon off;'

service nginx start
tail -f /var/log/nginx/*
