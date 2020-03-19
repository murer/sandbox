#!/bin/bash -xe

# openssl rand 66 | base64 -w0
vpn_ipsec_secret='WfXTHm026amU1fVwnXdmiTw/405NtDiqVFNiNM+RMmNRvIMK/+3uRBBful0ImtdWNid4j9cr176mAOGy7K3mxyun'

vpn_ipsec_me=10.0.20.209
vpn_ipsec_other=10.0.20.208
if hostname -I | grep 10.0.20.208; then
  vpn_ipsec_me=10.0.20.208
  vpn_ipsec_other=10.0.20.209
fi

echo "ipsec $vpn_ipsec_me -> $vpn_ipsec_other"

cp /etc/sysctl.conf /etc/sysctl.conf.bak
sed -i -r 's/#?(net.ipv4.ip_forward)\s*=.*/\1=1/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv6.conf.all.forwarding)\s*=.*/\1=1/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv4.conf.all.accept_redirects)\s*=.*/\1=0/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv4.conf.all.send_redirects)\s*=.*/\1=0/g' /etc/sysctl.conf
sysctl -p

apt-get -y update
apt-get -y install strongswan

echo "$vpn_instance_u1 $vpn_instance_u2 : PSK \"$vpn_ipsec_secret\"" > /etc/ipsec.secrets

# systemctl status strongswan.service
# systemctl is-enabled strongswan.service
