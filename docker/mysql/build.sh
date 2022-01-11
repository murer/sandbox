#!/bin/bash -xe

workdir="$( cd "$(dirname "$0")" ; pwd -P )"

cmd_clean() {
  docker rm -f murer-sandbox-mysql-server || true 
}

cmd_image() {
  docker build -t murer/sandbox-mysql .
}

cmd_run() {
  docker rm -f murer-sandbox-mysql-server || true
  docker run -it -p 127.0.0.1:3306:3306 --name murer-sandbox-mysql-server --rm murer/sandbox-mysql "$@"
}

cmd_exec() {
  docker exec -it murer-sandbox-mysql-server "$@"
}

cmd_logs() {
  cmd_exec mysql -u root -proot --execute "select * from mysql.general_log $1;" 
}

cmd_client() {
  cmd_exec mysql -u it -pit it
}

cd "$workdir"
_cmd=${1?'command'}
shift
cmd_${_cmd} "$@"
cd -


# RUN service mysql start && \
#     mysqladmin -u root password root && sleep 3 && \
#     mysql -u root -proot --execute "DELETE FROM mysql.user WHERE User='';" && \
#     mysql -u root -proot --execute "DROP DATABASE IF EXISTS test;" && \
#     mysql -u root -proot --execute "DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';" && \
#     mysql -u root -proot --execute "CREATE DATABASE it DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;" && \
#     mysql -u root -proot --execute "GRANT ALL ON it.* TO 'it'@'%' IDENTIFIED BY 'it';" && \
#     mysql -u root -proot --execute "FLUSH PRIVILEGES;" && \
#     mysql -u root -proot --execute "GRANT ALL ON mysql.* TO 'mydb'@'%';" && \
#     service mysql stop && sleep 3
#
# ADD config /opt/config
#
# EXPOSE 3306
#
# CMD [ "/usr/sbin/mysqld" ]

#     mysql -u root -proot --execute "GRANT ALL ON innodb.* TO 'mydb'@'%';" && \
