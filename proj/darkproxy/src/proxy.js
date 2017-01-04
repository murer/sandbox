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
        darkutil.loadBody(resp, commandResp, (body) => {
            console.log('body loaded', body);
        });
    });
}

module.exports = proxy;
