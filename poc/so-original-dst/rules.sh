#!/bin/bash -xe

#iptables -t nat -A PREROUTING -p tcp --dport 6000 -j REDIRECT --to-port 5000

iptables -t nat -A PREROUTING -s 192.168.56.0/24 -p tcp --dport 6000 -j DNAT --to-destination 127.0.0.1:5000
iptables -t nat -A OUTPUT -p tcp --dport 6000 -j DNAT --to-destination 127.0.0.1:5000

