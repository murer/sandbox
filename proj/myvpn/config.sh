#!/bin/bash -xe

apt-get -y update
apt-get -y install pptpd

echo "localip 10.3.3.1" >> /etc/pptpd.conf
echo "remoteip 10.3.3.100-200" >> /etc/pptpd.conf

echo "ms-dns 8.8.8.8" >> /etc/ppp/pptpd-options
echo "ms-dns 8.8.4.4" >> /etc/ppp/pptpd-options

echo 'guest * guest *' >> /etc/ppp/chap-secrets

echo 1 > /proc/sys/net/ipv4/ip_forward

iptables -t nat -A POSTROUTING -s 10.3.3.0/24 -o ens4 -j MASQUERADE
iptables -A FORWARD -p tcp --syn -s 10.3.3.0/24 -j TCPMSS --set-mss 1356

iptables -t nat -I PREROUTING -p tcp --dport 3389 -j REDIRECT --to-ports 1723

service pptpd restart
