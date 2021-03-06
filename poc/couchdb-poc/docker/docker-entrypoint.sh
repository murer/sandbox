#!/bin/bash -xe

echo '[cluster]' > /opt/couchdb/etc/default.d/20-cluster.ini
echo 'q = 8' >> /opt/couchdb/etc/default.d/20-cluster.ini
echo 'n = 2' >> /opt/couchdb/etc/default.d/20-cluster.ini
echo "seedlist = couchdb@couchdb1.${db_dns},couchdb@couchdb2.${db_dns}" >> /opt/couchdb/etc/default.d/20-cluster.ini

echo "-name couchdb@${db_name}.${db_dns}" >> /opt/couchdb/etc/vm.args
echo '-setcookie monster' >> /opt/couchdb/etc/vm.args

echo '[admins]' > /opt/couchdb/etc/local.d/admins.ini
echo 'admin = 123' >> /opt/couchdb/etc/local.d/admins.ini

/docker-entrypoint-original.sh /opt/couchdb/bin/couchdb

# sleep 2
#
# curl -vX PUT 'http://localhost:5984/_node/_local/_config/admins/admin' -d '"123"'
#
#
# curl -vX POST -H "Content-Type: application/json" \
#   'http://admin:123@127.0.0.1:5984/_cluster_setup' \
#   -d '{
#     "action": "enable_cluster",
#     "bind_address":"0.0.0.0",
#     "username": "admin",
#     "password":"password",
#     "node_count":"3"
#   }'
#
# curl http://admin:123@172.25.0.2:5984/_cluster_setup
#
# curl -X POST -H "Content-Type: application/json" \
#   http://admin:123@127.0.0.1:5984/_cluster_setup \
#   -d '{"action": "enable_cluster", "bind_address":"0.0.0.0", "username": "admin", "password":"password", "node_count":"2"}'
#
# fg
