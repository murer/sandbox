const http = require('http');
const fs = require('fs');

function _error(resp, err) {
    console.log('error on request', err);
    resp.statusCode = 500;
    resp.statusMessage = 'Internal Error';
    resp.removeHeader('Content-Length')
    resp.setHeader('Content-Type', 'text/plain; charset=UTF-8');
    resp.end('' + err);
}

function _writeRequestMeta(msg, success) {
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
    fs.writeFile(msg.server.dest + '/noop.json', JSON.stringify(meta), (err) => {
        if(err) _error(msg.resp, err)
        else success()
    })
}

function _loadRequest(msg, success) {
    var body = '';
    msg.req.on('aborted', (err) => {
        _error(msg.resp, 'client left')
    })
    msg.req.on('data', (data) => {
        body += data;
    })
    msg.req.on('end', () => {
        msg.req.body = body;
        success()
    })
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
    _loadRequest(message, () => {
        console.log('request loaded', req.body.length);
    })
}

function Server() {
    this.dest = 'target/requests'
}
Server.prototype.serve = function(port) { serve(this, port) }
Server.prototype.onRequest = function(req, resp) { onRequest(this, req, resp) }

function main() {
    var server = new Server()
    server.serve()
}

if (require.main === module) {
    main()
}
