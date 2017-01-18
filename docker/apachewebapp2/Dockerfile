FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update -y
RUN apt-get install -y apache2 libapache2-mod-wsgi \
                        python-pip \
                        curl wget vim nmap

RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf

RUN pip install WebOb
RUN pip install Paste
RUN pip install webapp2

RUN a2enmod wsgi

ADD src/apache/site.conf /etc/apache2/sites-available/site.conf
ADD src/app /home/app

RUN a2dissite 000-default.conf
RUN a2ensite site.conf

ADD src/apache/run.sh /run.sh
RUN chmod +x /run.sh

CMD /run.sh
