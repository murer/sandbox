FROM murer/hexblade:edge

USER root

RUN apt-get -y update
RUN apt-get -y install git wget xauth net-tools openssh-server
# RUN rm -rf /opt/hexblade && \
#     git clone https://github.com/murer/hexblade /opt/hexblade && \
#     cd /opt/hexblade && \
#     ./packages/golang/install-golang.sh && \
#     ./packages/vscode/install-vscode.sh && \
#     chown -R root:root /opt && \
#     chmod 4755 /opt/VSCode-linux-x64/chrome-sandbox

USER hexblade
WORKDIR /home/hexblade
RUN mkdir -p .vscode openerssh go/packages

RUN wget --progress=dot -e dotbytes=1M -c \
        'https://go.microsoft.com/fwlink/?LinkID=620884' \
        -O vscode.tar.gz && \
    tar xzf vscode.tar.gz && \
    rm vscode.tar.gz && \
    mv VSCode-linux-x64 vscode

RUN cd go && \
    wget --progress=dot -e dotbytes=1M -c \
        'https://golang.org/dl/go1.15.3.linux-amd64.tar.gz' \
        -O go.tar.gz && \
    tar xzf go.tar.gz && \
    rm go.tar.gz

ENV GOROOT "$HOME/go/go"
ENV GOPATH "$HOME/go/packages"
ENV PATH "$HOME/vscode/bin:$GOROOT/bin:$PATH"

RUN go get -u github.com/ramya-rao-a/go-outline
RUN go get -u github.com/go-delve/delve/cmd/dlv

