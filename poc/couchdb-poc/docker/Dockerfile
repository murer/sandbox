FROM couchdb:2.3.1

#RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
#RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get -y update
RUN apt-get -y install net-tools nmap iputils-ping vim jq 

RUN cp /docker-entrypoint.sh /docker-entrypoint-original.sh
COPY docker-entrypoint.sh /docker-entrypoint.sh

#EXPOSE 5984

#CMD [ "tini", "--", "/docker-entrypoint.sh", "/opt/couchdb/bin/couchdb" ]

#CMD [ "tini", "--", "/bin/bash" ]
