FROM ubuntu:14.04

RUN sed -i "s/\/\/us\.archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list
RUN sed -i "s/\/\/archive\.ubuntu\.com/\/\/br.archive.ubuntu.com/g" /etc/apt/sources.list

RUN apt-get update
RUN apt-get install -y wget curl vim

RUN apt-get install -y ssh rsync

WORKDIR /opt
RUN wget -nv http://repoz.dextra.com.br/repoz/r/pub/jdk/oracle/jdk-8u20-linux-x64.tar.gz -O jdk.tar.gz
RUN tar xzf jdk.tar.gz
RUN ln -s jdk1.8.0_20 jdk

ENV JAVA_HOME /opt/jdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN wget -nv http://ftp.unicamp.br/pub/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz -O hadoop.tar.gz
RUN tar xzf hadoop.tar.gz
RUN ln -s hadoop-2.7.3 hadoop

ENV PATH "/opt/hadoop/bin:/opt/hadoop/sbin:${PATH}"

RUN apt-get install -y openssh-server

RUN ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa
RUN cp /root/.ssh/id_rsa.pub /root/.ssh/authorized_keys
RUN chmod 0600 /root/.ssh/authorized_keys

RUN echo JAVA_HOME=$JAVA_HOME >> /etc/bash.bashrc.jdk
RUN echo PATH=\$JAVA_HOME/bin:\$PATH >> /etc/bash.bashrc.jdk
RUN	echo "source /etc/bash.bashrc.jdk" | sudo tee -a /etc/bash.bashrc

RUN sed --in-place='.ori' -e "s/\${JAVA_HOME}/\/opt\/jdk/" /opt/hadoop/etc/hadoop/hadoop-env.sh

WORKDIR /opt
ADD config /opt/config
RUN cp -v config/hadoop/core-site.xml config/hadoop/hdfs-site.xml hadoop/etc/hadoop
RUN chmod +x /opt/config/test.sh /opt/config/test2.sh

RUN echo 'Host *' > /root/.ssh/config
RUN echo '    StrictHostKeyChecking no' >> /root/.ssh/config

WORKDIR /root
