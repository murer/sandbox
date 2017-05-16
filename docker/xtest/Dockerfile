FROM ubuntu:14.04

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get update
RUN apt-get install -y wget curl xterm firefox xvfb x11vnc
RUN apt-get install -y vim

RUN mkdir /root/.vnc && x11vnc -storepasswd 1234 /root/.vnc/passwd

ADD config /opt/config

RUN chmod +x /opt/config/config.sh

CMD "/opt/config/config.sh"



# Xvfb :99 -ac
# x11vnc -display :99 -localhost &
# x11vnc -display :99 -forever -nopw
