FROM debian:10

SHELL ["/bin/sh", "-c"]

ENV LANG=C.UTF-8

RUN apt-get -y update
RUN DEBIAN_FRONTEND=noninteractive apt-get install -yq --no-install-recommends \
      ca-certificates \
      openssh-server \
      wget \
      vim \
      curl

RUN sed 's/session\s*required\s*pam_loginuid.so/session optional pam_loginuid.so/g' -i /etc/pam.d/sshd

RUN rm -rf /etc/update-motd.d /etc/motd /etc/motd.dynamic
RUN ln -fs /dev/null /run/motd.dynamic

# RUN curl -s https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | bash -xe

RUN wget --content-disposition --progress=dot -e dotbytes=5M \
  https://packages.gitlab.com/gitlab/gitlab-ce/packages/debian/buster/gitlab-ce_12.2.5-ce.0_amd64.deb/download.deb \
  -O /tmp/gitlab.deb

RUN dpkg -i /tmp/gitlab.deb
