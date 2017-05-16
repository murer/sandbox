FROM ubuntu:14.04

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get update
RUN apt-get install -y wget curl xterm firefox xvfb x11vnc

RUN mkdir /root/.vnc && x11vnc -storepasswd 1234 /root/.vnc/passwd

RUN apt-get install -y git

RUN git clone https://github.com/murer/sandbox /root/sandbox

WORKDIR /root/sandbox/configs/murer-ubuntu

RUN ./install.sh

CMD [ "x11vnc", "-forever", "-usepw", "-create" ]
