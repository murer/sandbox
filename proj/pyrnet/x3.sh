#!/bin/bash -xe

pyrnet_name="PYRNET"
pyrnet_device="$(nmcli -g TYPE,DEVICE d | grep '^wifi:' | cut -d':' -f2)"

nmcli -g NAME,UUID c | grep ^"$pyrnet_name": | cut -d':' -f2 | xargs nmcli con delete || true
sleep 1
nmcli con add type wifi ifname "$pyrnet_device" con-name "$pyrnet_name" autoconnect yes ssid "$pyrnet_name" -- \
	802-11-wireless.mode ap \
	802-11-wireless.band bg \
	ipv4.method shared \
	ipv4.addresses 192.168.240.1/24 \
	wifi-sec.key-mgmt wpa-psk \
	wifi-sec.psk 'PYRNET78'
sleep 1
nmcli con up "$pyrnet_name"
