FROM ubuntu:18.04

ENV LANG en_US.UTF-8

RUN apt-get -y update
RUN apt-get -y install wget curl vim net-tools nmap netcat
RUN apt-get -y install python3-pip

RUN adduser --disabled-login --gecos px px
#RUN chown -R proxy:proxy /home/px

USER px
WORKDIR /home/px
RUN mkdir bin
RUN pip3 install mitmproxy
COPY entrypoint/entrypoint.sh /home/px/bin/entrypoint.sh

#USER root
#RUN apt-get -y install network-manager
#RUN apt-get -y install sudo

#RUN groupadd -r supersudo && echo "%supersudo ALL=(ALL:ALL) NOPASSWD: ALL" > /etc/sudoers.d/supersudo
#RUN usermod -a -G supersudo px

#USER px

CMD ./bin/entrypoint.sh
