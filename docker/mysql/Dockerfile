FROM ubuntu:18.04

RUN apt-get update
RUN apt-get install -y wget curl
#RUN apt-get install -y language-pack-en-base
#RUN apt-get install -y language-pack-pt-base

RUN apt-get install -y mysql-client mysql-server && service mysql stop && sleep 3

RUN usermod -d /var/lib/mysql/ mysql

RUN sed -i.original 's/^bind-address.*/bind-address=0.0.0.0/g' /etc/mysql/my.cnf

RUN service mysql start && \
    mysqladmin -u root password root && sleep 3 && \
    mysql -u root -proot --execute "DELETE FROM mysql.user WHERE User='';" && \
    mysql -u root -proot --execute "DROP DATABASE IF EXISTS test;" && \
    mysql -u root -proot --execute "DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';" && \
    mysql -u root -proot --execute "CREATE DATABASE it DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;" && \
    mysql -u root -proot --execute "GRANT ALL ON it.* TO 'it'@'%' IDENTIFIED BY 'it';" && \
    mysql -u root -proot --execute "FLUSH PRIVILEGES;" && \
    mysql -u root -proot --execute "SET global general_log = 1;" && \
    mysql -u root -proot --execute "SET global log_output = 'table';" && \
    service mysql stop && sleep 3

ADD config /opt/config

EXPOSE 3306

# CMD [ "/usr/sbin/mysqld" ]

CMD [ "/usr/bin/mysqld_safe" ]
