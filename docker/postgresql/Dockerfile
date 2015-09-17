FROM ubuntu:14.04

RUN apt-get update && apt-get install -y python-software-properties software-properties-common 
RUN apt-get install -y language-pack-en-base
RUN apt-get install -y language-pack-pt-base 

ENV LANGUAGE en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

RUN apt-get install -y postgresql-9.3 postgresql-client-9.3 postgresql-contrib-9.3

ADD config /opt/config

RUN  find /opt/config -name "*.sh" -exec chmod +x "{}" \;

USER postgres

RUN /etc/init.d/postgresql start && \
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" && \
    createdb -E UTF8 -T template0 -O docker docker

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf

RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf

EXPOSE 5432

VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]



