FROM couchdb:2.3.1

#RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
#RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get -y update
RUN apt-get -y install net-tools nmap iputils-ping

#EXPOSE 5984

# CMD couchdb
