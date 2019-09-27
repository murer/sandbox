#!/bin/bash -xe

export DEBIAN_FRONTEND=noninteractive

gcp_inst_name="$(hostname)"
gcp_project="$(curl -H "Metadata-Flavor: Google" 'http://metadata.google.internal/computeMetadata/v1/project/project-id')"

apt-get -y update
apt-get -y install \
     apt-transport-https \
     ca-certificates \
     curl \
     gnupg2 \
     software-properties-common \
     nmap htop

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

apt-get -y update
apt-get -y install docker-ce

curl https://sdk.cloud.google.com | \
    CLOUDSDK_CORE_DISABLE_PROMPTS=1 CLOUDSDK_INSTALL_DIR="$/root/opt" bash > /dev/null
export PATH="/root/opt/google-cloud-sdk/bin:$PATH"
/root/opt/google-cloud-sdk/bin/gcloud config set disable_usage_reporting true
/root/opt/google-cloud-sdk/bin/gcloud components install docker-credential-gcr -q
/root/opt/google-cloud-sdk/bin/gcloud auth configure-docker -q

docker run \
  -d --rm --name "$gcp_inst_name" \
  -h "$gcp_inst_name" \
  -e "db_name=$gcp_inst_name" \
  -e "db_dns=c.dxtdna.internal" \
  -p 5984:5984 \
  -p 5986:5986 \
  -p 4369:4369 \
  'gcr.io/dxtdna/couchdb:latest'

#
# echo "deb https://apache.bintray.com/couchdb-deb "$(lsb_release -cs)" main" \
#     > /etc/apt/sources.list.d/couchdb.list
# curl -L https://couchdb.apache.org/repo/bintray-pubkey.asc | apt-key add -
#
# apt-get -y update
# apt-get -y install net-tools nmap iputils-ping vim jq couchdb
#
# service couchdb stop
#
# echo '[chttpd]' > /opt/couchdb/etc/default.d/20-chttpd.ini
# echo 'bind_address = any' >> /opt/couchdb/etc/default.d/20-chttpd.ini
# echo '[httpd]' >> /opt/couchdb/etc/default.d/20-chttpd.ini
# echo 'bind_address = any' >> /opt/couchdb/etc/default.d/20-chttpd.ini
#
# echo "[cluster]" > /opt/couchdb/etc/default.d/20-cluster.ini
# echo "q = 8" >> /opt/couchdb/etc/default.d/20-cluster.ini
# echo "n = 2" >> /opt/couchdb/etc/default.d/20-cluster.ini
# echo "seedlist = couchdb@couchdb1.c.${gcp_project}.internal,couchdb@couchdb2.c.${gcp_project}.internal" >> /opt/couchdb/etc/default.d/20-cluster.ini
#
# cp /opt/couchdb/etc/vm.args /opt/couchdb/etc/vm.args.bak
# grep -v '^-name' /opt/couchdb/etc/vm.args.bak | grep -v '^-setcookie' > /opt/couchdb/etc/vm.args
# echo "-name couchdb@${gcp_inst_name}.c.${gcp_project}.internal" >> /opt/couchdb/etc/vm.args
# echo '-setcookie monster' >> /opt/couchdb/etc/vm.args
#
# echo '[admins]' > /opt/couchdb/etc/local.d/admins.ini
# echo 'admin = 123' >> /opt/couchdb/etc/local.d/admins.ini
#
# (service couchdb start &)
