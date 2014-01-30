#!/bin/bash -xe

mkdir -p gen/app 
cd gen/app

if [ ! -f libaio.deb ]; then wget -O libaio.deb --progress=dot https://oss.oracle.com/debian/dists/unstable/main/binary-i386/libaio_0.3.104-1_i386.deb; fi 
if [ ! -f oracle.deb ]; then wget -O oracle.deb --progress=dot https://oss.oracle.com/debian/dists/unstable/non-free/binary-i386/oracle-xe_10.2.0.1-1.1_i386.deb; fi 

# fixing swap
if [ ! -f /swpfs1 ]; then
	sudo dd if=/dev/zero of=/swpfs1 bs=1M count=1500
	sudo mkswap /swpfs1
	sudo swapon /swpfs1;
fi;

# fixing /dev/shm
if ! mount | grep /dev/shm; then
	sudo rm -rf /dev/shm
	sudo mkdir /dev/shm
	sudo mount --move /run/shm /dev/shm
	sudo mount -B /dev/shm /run/shm
fi;

if [ -d oracle ]; then rm -rf oracle; fi

mkdir oracle
cd -
cd gen/app/oracle

# install
sudo apt-get -y install bc
sudo dpkg -i ../libaio.deb
sudo dpkg -i ../oracle.deb

# config
sudo chown -R oracle:dba /var/tmp/.oracle /tmp/.oracle
sudo chmod -R 1777 /var/tmp/.oracle /tmp/.oracle
set +e
sudo su - oracle -c "/usr/lib/oracle/xe/app/oracle/product/10.2.0/server/bin/lsnrctl stop"
sudo su - oracle -c "/usr/lib/oracle/xe/app/oracle/product/10.2.0/server/bin/lsnrctl start"
set -e

cat <<-EOF > /tmp/tmp.bash_profile
ORACLE_HOME=/usr/lib/oracle/xe/app/oracle/product/10.2.0/server
PATH=\$PATH:\$ORACLE_HOME/bin
export ORACLE_HOME
export ORACLE_SID=XE
export PATH
EOF
sudo mv /tmp/tmp.bash_profile ~oracle/.bash_profile
sudo chown oracle:dba ~oracle/.bash_profile

sudo su - oracle -c "echo export ORACLE_HOME=/usr/lib/oracle/xe/app/oracle/product/10.2.0/server >> /usr/lib/oracle/xe/app/oracle/product/10.2.0/server/bin/oracle_env.sh"
sudo su - oracle -c "echo export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:\$ORACLE_HOME/lib >> /usr/lib/oracle/xe/app/oracle/product/10.2.0/server/bin/oracle_env.sh"
sudo su - -c "echo /usr/lib/oracle/xe/app/oracle/product/10.2.0/server/lib >> /etc/ld.so.conf"
sudo /sbin/ldconfig

echo -e "7070\n\nportal\nportal\n\n" | sudo /etc/init.d/oracle-xe configure

cat <<-EOF | sudo su - oracle -c "sqlplus sys/portal as sysdba"
CREATE USER portal IDENTIFIED BY portal;
GRANT CONNECT TO portal;
alter user portal quota 5g on users;
GRANT create session TO portal;
GRANT create table TO portal;
GRANT create view TO portal;
GRANT create any trigger TO portal;
GRANT create any procedure TO portal;
GRANT create sequence TO portal;
GRANT create synonym TO portal;
alter database default tablespace users;
EOF

cd -






