#!/bin/bash -xe

Xvfb :99 -screen 0 1024x768x24 -ac &
sleep 2
openbox --startup /etc/X11/openbox/autostart &
x11vnc -display :99 -forever -nopw
