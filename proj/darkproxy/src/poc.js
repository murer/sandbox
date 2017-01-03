const http = require('http');
const fs = require('fs');
const UUID = require('./uuid');

function _error(resp, err) {
    console.log('error on request', err);
    resp.statusCode = 500;
    resp.statusMessage = 'Internal Error';
    resp.removeHeader('Content-Length')
    resp.setHeader('Content-Type', 'text/plain; charset=UTF-8');
    resp.end('' + err);
}

function _loadTarget(msg) {
    var req = {
        method: msg.req.method,
        uri: msg.req.url,
        remote: {
            host: null,
            port: 80
        },
        headers: msg.req.headers
    }
    if(msg.req.headers.host) {
        var array = msg.req.headers.host.split(':')
        req.remote.host = array[0]
        if(array.length > 1) {
            req.remote.port = parseInt(array[1]);
        }
    }
    msg.data.req = req;
}

function _loadRequest(msg, success) {
    msg.data = { id: new UUID().format() };
    _loadTarget(msg);
    var body = '';
    msg.req.on('aborted', (err) => {
        _error(msg.resp, 'client left')
    })
    msg.req.on('data', (data) => {
        body += data;
    })
    msg.req.on('end', () => {
        msg.data.req.body = body;
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
    var msg = { server: self, req: req, resp: resp }
    _loadRequest(msg, () => {
        console.log('request loaded', msg.data);
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
