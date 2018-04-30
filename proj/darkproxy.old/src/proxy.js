const http = require('http');
const darkutil = require('./util');

function proxy(self, msg, commandReq, commandResp) {
    var opts = {
        hostname: 'google.com',
        port: 80,
        path: msg.data.req.uri,
        method: msg.data.req.method,
        headers: {}
    }
    for(var name in msg.data.req.headers) {
        var value = msg.data.req.headers[name];
        if(name == 'content-length') {
            value = darkutil.b64len(msg.data.req.body).toString();
        }
        opts.headers[name] = value;
    }
    console.log('proxy', opts);
    var req = http.request(opts, (resp) => {
        console.log('reading response');
        msg.data.resp = {
            created: new Date().getTime(),
            code: resp.statusCode,
            reason: resp.statusMessage,
            headers: resp.headers
        };
        resp.setEncoding('base64');
        darkutil.loadBody(resp, commandResp, (body) => {
            msg.data.resp.body = body;
            console.log('proxy resp', msg.data.resp);
            darkutil.sendJson(commandResp, msg.data);
        });
    });
    req.on('error', (err) => {
        console.log('error send request proxy', err);
        darkutil.sendError(commandResp, err);
    });
    if(msg.data.req.body) {
        console.log('sending request body', msg.data.req.body.length);
        req.write(msg.data.req.body, 'base64');
    }
    req.end();
}

function sendResp(self, msg, commandReq, commandResp) {
    msg.resp.statusCode = msg.data.resp.code;
    msg.resp.statusMessage = msg.data.resp.reason;
    console.log('sending response', msg.data.id, msg.data.resp.code, msg.data.resp.reason);
    for(var name in msg.data.resp.headers) {
        var value = msg.data.resp.headers[name];
        if (name == 'content-length') {
            value = darkutil.b64len(msg.data.resp.body).toString();
        }
        msg.resp.setHeader(name, value);
    }
    msg.resp.end(msg.data.resp.body, 'base64', () => {
        self.msgs.remove(msg.data.id);
        darkutil.sendJson(commandResp, 'OK');
    });
}

exports.proxy = proxy;
exports.sendResp = sendResp;
