FROM ubuntu:18.04

RUN apt-get -y update
RUN apt-get -y install nmap netcat net-tools wget curl vim
RUN apt-get -y install sudo zip

RUN mkdir /opt/bin
ENV PATH "$PATH:/opt/bin"
WORKDIR /opt/bin
RUN wget --progress=dot -e dotbytes=256K -O vault.zip \
  'https://releases.hashicorp.com/vault/1.3.2/vault_1.3.2_linux_amd64.zip' && \
  unzip vault.zip && \
  rm vault.zip

RUN groupadd -r supersudo && echo "%supersudo ALL=(ALL:ALL) NOPASSWD: ALL" > /etc/sudoers.d/supersudo
RUN adduser --disabled-login --gecos vault vault
RUN usermod -a -G supersudo vault

USER vault
WORKDIR /home/vault
RUN vault -h
