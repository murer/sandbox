FROM debian:10

SHELL ["/bin/sh", "-c"]

ENV LANG=C.UTF-8
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get -y update
RUN apt-get install -yq --no-install-recommends \
      ca-certificates openssh-server wget curl vim git

RUN wget --progress=dot -e dotbytes=1M -O /usr/local/bin/gitea https://dl.gitea.io/gitea/1.11.5/gitea-1.11.5-linux-amd64 && \
  chmod +x /usr/local/bin/gitea

RUN adduser --system --shell /bin/bash --gecos 'Git Version Control' \
   --group --disabled-password --home /home/git git

ENV GITEA_WORK_DIR /var/lib/gitea

RUN mkdir "$GITEA_WORK_DIR" && chown -R git:git "$GITEA_WORK_DIR"

USER git

CMD [ "gitea", "web" ]
