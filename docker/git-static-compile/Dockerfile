FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y wget curl
RUN apt-get install -y build-essential

#RUN apt-get install -y git

WORKDIR /opt/proj

RUN wget -nv https://github.com/git/git/archive/v2.16.2.zip -O git.zip

RUN apt-get install -y zip
RUN apt-get install -y libssl-dev
RUN apt-get install -y libcurl4-openssl-dev
RUN apt-get install -y libexpat1-dev
RUN apt-get install -y gettext

RUN unzip git.zip
RUN mv git-2.16.2 git

WORKDIR /opt/proj/git

RUN make prefix=/opt/git-static CFLAGS="${CFLAGS} -static-libgcc"

CMD /bin/bash
