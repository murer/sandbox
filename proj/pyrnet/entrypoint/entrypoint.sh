#!/bin/bash -xe

pyrnet_name="PYRNET"
pyrnet_device="$(nmcli -g TYPE,DEVICE d | grep '^wifi:' | cut -d':' -f2)"
