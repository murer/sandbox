FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update -y
RUN apt-get install -y curl wget vim nmap \
                        python-pip

RUN apt-get install -y nginx

ADD src/nginx/run.sh /run.sh
RUN chmod +x /run.sh

CMD /run.sh
