FROM ubuntu:14.04

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get -y update
RUN apt-get -y install software-properties-common
RUN add-apt-repository -y ppa:couchdb/stable
RUN apt-get -y update

RUN apt-get install -y couchdb

RUN mkdir /var/run/couchdb

# RUN service couchdb stop

#EXPOSE 5984

CMD couchdb
