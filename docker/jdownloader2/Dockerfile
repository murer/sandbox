FROM murer/hexblade:latest

USER root

RUN apt-get -y update
RUN apt-get -y install wget curl

WORKDIR /opt
RUN wget  --progress=dot -e dotbytes=1M -c -O jdk.tar.gz \
  'http://repoz.dextra.com.br/repoz/r/pub/jdk/oracle/jdk-8u221-linux-x64.tar.gz' && \
  tar xzf jdk.tar.gz && \
  mv jdk1.8.0_221 jdk && \
  rm jdk.tar.gz
ENV JAVA_HOME /opt/jdk
ENV PATH "$JAVA_HOME/bin:$PATH"

USER hexblade
WORKDIR /home/hexblade
RUN mkdir -p opt/JDownloader
RUN wget  --progress=dot -e dotbytes=32K -c -O opt/JDownloader/JDownaloder.jar \
  http://installer.jdownloader.org/JDownloader.jar
