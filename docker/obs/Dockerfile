FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get -y update
RUN apt-get -y install \
  software-properties-common \
  python-software-properties

RUN add-apt-repository "deb http://archive.ubuntu.com/ubuntu $(lsb_release -sc) multiverse"
RUN add-apt-repository ppa:kirillshkrogalev/ffmpeg-next
RUN add-apt-repository ppa:obsproject/obs-studio
RUN apt-get -y update

RUN apt-get -y install \
  ffmpeg \
  obs-studio

RUN mkdir -p /home/obs
RUN echo "obs:x:1000:1000:OpenBroadcastSoftware,,,:/home/obs:/bin/bash" >> /etc/passwd
RUN echo "obs:x:1000:" >> /etc/group
RUN chown obs:obs -R /home/obs

USER obs
ENV HOME /home/obs

CMD ["obs"]
