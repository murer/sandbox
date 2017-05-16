FROM ubuntu:14.04

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get update
RUN apt-get install -y wget curl xterm firefox xvfb x11vnc
RUN apt-get install -y vim
RUN apt-get install -y openbox tint2
RUN apt-get install -y lxterminal
RUN apt-get install -y gmrun
RUN apt-get install -y pcmanfm

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add - && \
    echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | sudo tee /etc/apt/sources.list.d/google-chrome.list && \
    sudo apt-get update && \
    sudo apt-get -y install google-chrome-stable

RUN mkdir /root/.vnc && x11vnc -storepasswd 1234 /root/.vnc/passwd

ADD config /opt/config

RUN chmod +x /opt/config/config.sh /opt/config/openbox/autostart

RUN rm -rf /etc/xdg/openbox && \
    cp -R /opt/config/openbox /etc/xdg/openbox

ENV DISPLAY :99

CMD "/opt/config/config.sh"

# Xvfb :99 -ac
# x11vnc -display :99 -localhost &
# x11vnc -display :99 -forever -nopw
