#!/bin/bash -xe

pyrnet_name="PYRNET"
pyrnet_device="$(nmcli -g TYPE,DEVICE d | grep '^wifi:' | cut -d':' -f2)"

cmd_wifi() {
	nmcli -g NAME,UUID c | grep ^"$pyrnet_name": | cut -d':' -f2 | xargs nmcli con delete || true
	sleep 1
	nmcli con add type wifi ifname "$pyrnet_device" con-name "$pyrnet_name" autoconnect yes ssid "$pyrnet_name" -- \
		802-11-wireless.mode ap \
		802-11-wireless.band bg \
		ipv4.method shared \
		ipv4.addresses 192.168.240.1/24 \
		ipv6.method ignore \
		wifi-sec.key-mgmt wpa-psk \
		wifi-sec.psk 'PYRNET78'
	sleep 1
	nmcli con up "$pyrnet_name"
}

cmd_remove() {
	iptables -t nat -v -L PREROUTING -n --line-number

	sudo iptables -t nat -v -L PREROUTING -n --line-number | grep '8080$' | \
		cut -d' ' -f1 | tac | while read k; do \
			sudo iptables -t nat -D PREROUTING "$k";
		done
}

cmd_redirect() {
	cmd_remove
	sysctl -w net.ipv4.ip_forward=1
	#iptables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 80 -j REDIRECT --to-port 8080
	#iptables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 443 -j REDIRECT --to-port 8080
	#ip6tables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 80 -j REDIRECT --to-port 8080
	#ip6tables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 443 -j REDIRECT --to-port 8080

	iptables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 80 -j REDIRECT --to 172.17.0.2:8080
	iptables -t nat -A PREROUTING -i "$pyrnet_device" -p tcp --dport 443 -j REDIRECT --to 172.17.0.2:8080

}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
