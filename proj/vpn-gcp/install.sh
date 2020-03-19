#!/bin/bash -xe

# openssl rand 66 | base64 -w0
vpn_ipsec_secret='WfXTHm026amU1fVwnXdmiTw/405NtDiqVFNiNM+RMmNRvIMK/+3uRBBful0ImtdWNid4j9cr176mAOGy7K3mxyun'

vpn_ipsec_me_ip=10.0.20.209
vpn_ipsec_me_net=10.2.20.0/24
vpn_ipsec_other_ip=10.0.20.208
vpn_ipsec_other_net=10.1.20.0/24
if hostname -I | grep 10.0.20.208; then
  vpn_ipsec_me_ip=10.0.20.208
  vpn_ipsec_me_net=10.1.20.0/24
  vpn_ipsec_other_ip=10.0.20.209
  vpn_ipsec_other_net=10.2.20.0/24
fi

echo "ipsec $vpn_ipsec_me_ip $vpn_ipsec_me_net -> $vpn_ipsec_other_ip $vpn_ipsec_other_net"

cp /etc/sysctl.conf /etc/sysctl.conf.bak
sed -i -r 's/#?(net.ipv4.ip_forward)\s*=.*/\1=1/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv6.conf.all.forwarding)\s*=.*/\1=1/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv4.conf.all.accept_redirects)\s*=.*/\1=0/g' /etc/sysctl.conf
sed -i -r 's/#?(net.ipv4.conf.all.send_redirects)\s*=.*/\1=0/g' /etc/sysctl.conf
sysctl -p

apt-get -y update
apt-get -y install strongswan

echo "$vpn_ipsec_me $vpn_ipsec_other : PSK \"$vpn_ipsec_secret\"" > /etc/ipsec.secrets

cat > /etc/ipsec.conf << EOF
# basic configuration
config setup
  charondebug="all"
  uniqueids=yes
  strictcrlpolicy=no

# connection to amsterdam datacenter
conn paris-to-amsterdam
  authby=secret
  left=%defaultroute
  leftid=$vpn_ipsec_me_ip
  leftsubnet=$vpn_ipsec_me_net
  right=$vpn_ipsec_other_ip
  rightsubnet=$vpn_ipsec_other_net
  ike=aes256-sha2_256-modp1024!
  esp=aes256-sha2_256!
  keyingtries=0
  ikelifetime=1h
  lifetime=8h
  dpddelay=30
  dpdtimeout=120
  dpdaction=restart
  auto=start
EOF

iptables -t nat -A POSTROUTING -s "$vpn_ipsec_other_net" -d "$vpn_ipsec_me_net" -j MASQUERADE

ipsec restart

ipsec status

# systemctl status strongswan.service
# systemctl is-enabled strongswan.service
