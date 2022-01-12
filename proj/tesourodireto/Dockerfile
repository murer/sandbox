FROM murer/hexblade:latest

USER root
ENV HOME /root

WORKDIR /tmp

RUN mkdir -p /etc/apt/hardkeys && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub > /etc/apt/hardkeys/google-chrome.list && \
    echo 'deb [arch=amd64 signed-by=/etc/apt/hardkeys/google-chrome.list] http://dl.google.com/linux/chrome/deb/ stable main' > /etc/apt/sources.list.d/google-chrome.list && \
    apt update && \ 
    apt -y install google-chrome-stable

RUN apt-get -y install python3-pip xterm curl wget nmap vim zip

RUN wget -O chromedriver.zip --progress=dot -e dotbytes=64K "https://chromedriver.storage.googleapis.com/$(google-chrome --version | cut -d' ' -f3)/chromedriver_linux64.zip" && \
    unzip /tmp/chromedriver.zip && \
    cp -v chromedriver /usr/local/bin

#RUN chown -R hexblade:hexblade /home/hexblade

WORKDIR /home/hexblade
USER hexblade
ENV HOME /home/hexblade

RUN pip install selenium
