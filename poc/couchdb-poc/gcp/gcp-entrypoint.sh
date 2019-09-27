#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive

gcp_inst_name="$(hostname)"
gcp_project="$(curl -H "Metadata-Flavor: Google" 'http://metadata.google.internal/computeMetadata/v1/project/project-id')"

echo "deb https://apache.bintray.com/couchdb-deb "$(lsb_release -cs)" main" \
    > /etc/apt/sources.list.d/couchdb.list
curl -L https://couchdb.apache.org/repo/bintray-pubkey.asc | apt-key add -

apt-get -y update
apt-get -y install net-tools nmap iputils-ping vim jq couchdb

service couchdb stop

echo '[chttpd]' > /opt/couchdb/etc/local.d/chttpd.ini
echo 'bind_address = 127.0.0.1' >> /opt/couchdb/etc/local.d/chttpd.ini

echo "[cluster]" > /opt/couchdb/etc/default.d/20-cluster.ini
echo "q = 8" >> /opt/couchdb/etc/default.d/20-cluster.ini
echo "n = 2" >> /opt/couchdb/etc/default.d/20-cluster.ini
echo "seedlist = couchdb@couchdb1.c.${gcp_project}.internal,couchdb@couchdb2.c.${gcp_project}.internal" >> /opt/couchdb/etc/default.d/20-cluster.ini

echo "-name couchdb@${gcp_inst_name}.c.${gcp_project}.internal" >> /opt/couchdb/etc/vm.args
echo '-setcookie monster' >> /opt/couchdb/etc/vm.args

echo '[admins]' > /opt/couchdb/etc/local.d/admins.ini
echo 'admin = 123' >> /opt/couchdb/etc/local.d/admins.ini

tail -f /opt/couchdb/var/log/couchdb.log >> /dev/ttyS1

service couchdb start
