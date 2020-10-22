FROM murer/hexblade:latest

USER root

RUN apt-get -y update
RUN apt-get -y install wget curl git build-essential openssl libssl-dev

RUN curl -sL https://deb.nodesource.com/setup_15.x | sudo -E bash -
RUN apt-get install -y nodejs

RUN /opt/hexblade/packages/chrome/install-chrome.sh

USER hexblade
RUN mkdir proj

WORKDIR /home/hexblade
RUN git clone https://github.com/FaisalUmair/udemy-downloader-gui.git proj/uldeler
WORKDIR proj/uldeler
RUN git checkout v1.8.2
RUN npm install
