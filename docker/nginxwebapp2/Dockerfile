FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update -y
RUN apt-get install -y curl wget vim nmap \
                        python-pip

RUN apt-get install -y nginx
RUN apt-get install -y uwsgi uwsgi-plugin-python

ADD src/nginx/uwsgi.xml /etc/uwsgi/apps-available/uwsgi.xml
RUN ln -s /etc/uwsgi/apps-available/uwsgi.xml /etc/uwsgi/apps-enabled/uwsgi.xml

ADD src/nginx/site.conf /etc/nginx/sites-available/site.conf
RUN rm /etc/nginx/sites-enabled/default
RUN ln -s /etc/nginx/sites-available/site.conf /etc/nginx/sites-enabled/site.conf


ADD src/nginx/run.sh /run.sh
RUN chmod +x /run.sh

CMD /run.sh
