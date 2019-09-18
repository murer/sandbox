FROM debian:10

SHELL ["/bin/sh", "-c"]

ENV LANG=C.UTF-8
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -y update
RUN apt-get install -yq --no-install-recommends \
      ca-certificates openssh-server wget curl vim git

ENV GITEA_WORK_DIR /var/lib/gitea

RUN mkdir "${GITEA_WORK_DIR}"
WORKDIR "$GITEA_WORK_DIR"
RUN mkdir bin custom data log

RUN wget --progress=dot -e dotbytes=1M \
  -O bin/gitea \
  "https://dl.gitea.io/gitea/1.9.3/gitea-1.9.3-linux-amd64"
RUN chmod +x bin/gitea

#RUN apt-get -y install gpg
#RUN wget -O /opt/gitea/gitea.asc "https://dl.gitea.io/gitea/1.9.3/gitea-1.9.3-linux-amd64.asc"
#RUN gpg --keyserver pgp.mit.edu --recv 7C9E68152594688862D62AF62D9AE806EC1592E2
#RUN gpg --verify /opt/gitea/gitea.asc /opt/gitea/gitea

RUN adduser --system --shell /bin/bash --gecos 'Git Version Control' \
   --group --disabled-password --home /home/git git

RUN chown -R git:git "$GITEA_WORK_DIR" && \
    chmod -R 750 "$GITEA_WORK_DIR"

USER git

CMD [ "bin/gitea", "web" ]
