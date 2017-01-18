#!/bin/bash -xe

service apache2 restart
tail -f /var/log/apache2/*
