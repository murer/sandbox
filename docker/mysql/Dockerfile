FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y wget curl
#RUN apt-get install -y language-pack-en-base
#RUN apt-get install -y language-pack-pt-base

RUN apt-get install -y mysql-client mysql-server && service mysql stop && sleep 3

RUN service mysql start && \
    mysqladmin -u root password root && sleep 3 && \
    mysql -u root -proot --execute "SET GLOBAL log_bin_trust_function_creators=true;" && \
    mysql -u root -proot --execute "DELETE FROM mysql.user WHERE User='';" && \
    mysql -u root -proot --execute "DROP DATABASE IF EXISTS test;" && \
    mysql -u root -proot --execute "DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';" && \
    mysql -u root -proot --execute "CREATE DATABASE lumisportal;" && \
    mysql -u root -proot --execute "GRANT ALL ON lumisportal.* TO 'gptwadmin'@'%' IDENTIFIED BY 'gptwadmin';" && \
    mysql -u root -proot --execute "GRANT ALL ON innodb.* TO 'gptwadmin'@'%';" && \
    mysql -u root -proot --execute "GRANT ALL ON mysql.* TO 'gptwadmin'@'%';" && \
    mysql -u root -proot --execute "CREATE DATABASE gptwlistmanager DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;" && \
    mysql -u root -proot --execute "GRANT ALL ON gptwlistmanager.* TO 'gptwadmin'@'%' IDENTIFIED BY 'gptwadmin';" && \
    mysql -u root -proot --execute "FLUSH PRIVILEGES;" && \
    service mysql stop && sleep 3

EXPOSE 3309

CMD [" /usr/sbin/mysqld" ]
