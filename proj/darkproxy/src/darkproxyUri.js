const darkutil = require('./util');
const proxy = require('./proxy');

function sendData(self, req, resp) {
    var id = req.url.split('/')[3];
    var ret = self.msgs.get(id);
    if(ret) {
      ret = ret.data;
    }
    darkutil.sendJson(resp, ret);
}

function receiveData(self, req, resp) {
    darkutil.loadBody(req, resp, (body) => {
        body = JSON.parse(body);
        var msg = self.msgs.get(body.id);
        if(!msg) {
            darkutil.sendNotFound(resp);
            return;
        }
        console.log('request changed', body.id);
        msg.data = body;
        if(msg.data.resp) {
          sendResp(self, msg);
        } else {
          proxy(self, msg, req, resp);
        }
    })
}

function darkproxy(self, req, resp) {
    if(req.method == 'GET' && req.url == '/_darkproxy/request') {
        darkutil.sendJson(resp, self.msgs.toList());
    } else if(req.method == 'GET' && req.url.match(/^\/_darkproxy\/request\/[0-9a-fA-F\-]{36}$/)) {
        sendData(self, req, resp);
    } else if(req.method == 'POST' && req.url == '/_darkproxy/request') {
        receiveData(self, req, resp);
    } else {
        darkutil.sendNotFound(resp);
    }
}

module.exports = darkproxy
