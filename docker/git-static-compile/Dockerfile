FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y wget curl zip \
  build-essential \
  libssl-dev \
  libcurl4-openssl-dev \
  libexpat1-dev \
  gettext

RUN adduser --disabled-password --gecos '' git

USER git
RUN mkdir /home/git/proj
WORKDIR /home/git/proj

#RUN wget -nv https://github.com/git/git/archive/v2.16.2.zip -O git.zip
RUN wget -nv 'https://mirrors.edge.kernel.org/pub/software/scm/git/git-2.16.2.tar.gz' -O git.tar.gz

RUN tar xzf git.tar.gz
RUN mv git-2.16.2 git

WORKDIR /home/git/proj/git

RUN ./configure prefix=/home/git/git-static/ CFLAGS="${CFLAGS} -static-libgcc"
RUN make
RUN make install

RUN tar czf git-static.tar.gz git-static

CMD /bin/bash
