#!/bin/bash -xe

#nginx -g 'daemon off;'

service uwsgi start
service nginx start
tail -f /var/log/nginx/*
