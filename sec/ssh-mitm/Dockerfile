FROM ubuntu:18.04

RUN apt-get -y update && \
  apt-get install -y git vim curl wget net-tools nmap openssl netcat

RUN git clone https://github.com/jtesta/ssh-mitm /opt/ssh-mitm

WORKDIR /opt/ssh-mitm

RUN ./install.sh

WORKDIR /home/ssh-mitm
