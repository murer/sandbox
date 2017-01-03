const http = require('http');
const fs = require('fs');
const UUID = require('./uuid');
const MessageHolder = require('./messageHolder');
const darkproxyUri = require('./darkproxyUri');
const darkutil = require('./util');

function _loadTarget(msg) {
    var req = {
        created: new Date().getTime(),
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
    darkutil.requestLoad(msg.req, msg.resp, (body) => {
        msg.data.req.body = body;
        success()
    });
}

function serve(self, port) {
    self.server = http.createServer();
    self.server.on('request', (req, resp) => {
        self.onRequest(req, resp)
    })
    self.server.listen(8000);
}

function onRequest(self, req, resp) {
    if(req.url.startsWith('/_darkproxy/') || req.url == '/_darkproxy') {
        self.darkproxyUri(req, resp);
        return;
    }
    var msg = { server: self, req: req, resp: resp }
    _loadRequest(msg, () => {
        self.msgs.add(msg);
        console.log('request loaded', msg.data.id, msg.req.method, msg.req.url);
    })
}

function stop(self, cb) {
    self.server.close(cb)
}

function Server() {
    this.dest = 'target/requests';
    this.msgs = new MessageHolder();
}
Server.prototype.serve = function(port) { serve(this, port) };
Server.prototype.darkproxyUri = function(req, resp) { darkproxyUri(this, req, resp) };
Server.prototype.onRequest = function(req, resp) { onRequest(this, req, resp) };
Server.prototype.stop = function(cb) { stop(this, cb); }

function main() {
    var server = new Server()
    server.serve()
}

exports.Server = Server;

if (require.main === module) {
    main()
}
