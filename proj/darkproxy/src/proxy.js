const http = require('http');
const darkutil = require('./util');

function proxy(self, msg, commandReq, commandResp) {
    var opts = {
        hostname: 'google.com',
        port: 80,
        path: msg.data.req.uri,
        method: msg.data.req.method,
        headers: msg.data.req.headers
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
        req.write(msg.data.req.body);
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
            continue;
        }
        msg.resp.setHeader(name, value);
    }
    msg.resp.end(msg.data.resp.body, null, () => {
        darkutil.sendJson(commandResp, 'OK');
    });
}

exports.proxy = proxy;
exports.sendResp = sendResp;
