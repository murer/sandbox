#!/bin/bash -xe

Xvfb :99 -ac &
sleep 2
x11vnc -display :99 -forever -nopw
