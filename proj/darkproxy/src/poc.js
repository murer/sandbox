const http = require('http');

function _writeRequestMeta(msg) {
    var meta = {
        method: msg.req.method,
        uri: msg.req.url,
        local: {
            address: msg.req.socket.localAddress,
            port: msg.req.socket.localPort
        },
        remote: {
            family: msg.req.socket.remoteFamily,
            address: msg.req.socket.remoteAddress,
            port: msg.req.socket.remotePort
        },
        headers: msg.req.headers
    }
    console.log('x', meta)
}

function serve(self, port) {
    self.server = http.createServer();
    self.server.on('request', (req, resp) => {
        self.onRequest(req, resp)
    })
    self.server.listen(8000);
}

function onRequest(self, req, resp) {
    var message = { server: self, req: req, resp: resp }
    _writeRequestMeta(message, () => {
        console.log('file writed');
    });
}

function Server() {}
Server.prototype.serve = function(port) { serve(this, port) }
Server.prototype.onRequest = function(req, resp) { onRequest(this, req, resp) }

function main() {
    var server = new Server()
    server.serve()
}

if (require.main === module) {
    main()
}
