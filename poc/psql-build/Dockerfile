FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -y update
RUN apt-get -y install wget vim curl build-essential

RUN mkdir /proj
WORKDIR /proj

RUN wget --progres=dot "http://ftp.postgresql.org/pub/source/v9.5.2/postgresql-9.5.2.tar.gz" -O postgresql.tar.gz
RUN tar xzf postgresql.tar.gz
RUN mv postgresql-9.5.2 postgresql
RUN mkdir build

WORKDIR /proj/build

RUN apt-get -y install libreadline-dev zlib1g-dev

RUN /proj/postgresql/configure --prefix=/proj/target
RUN make
RUN make install

RUN addgroup supersudo
RUN adduser --disabled-password --gecos '' test
RUN adduser test supersudo
RUN echo '%supersudo ALL=(ALL) NOPASSWD:ALL' > /etc/sudoers.d/supersudo

ENV PATH /proj/target/bin:$PATH

USER test
WORKDIR /home/test

RUN initdb -D /home/test/db
RUN (postgres -D /home/test/db 1>/home/test/log 2>&1 &); \
    sleep 3 && cat /home/test/log && createdb test && \
    kill -INT $(head -1 /home/test/db/postmaster.pid)

CMD [ '/bin/bash' ]
