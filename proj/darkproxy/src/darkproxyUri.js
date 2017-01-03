const darkutil = require('./util');

function sendData(self, req, resp) {
    var id = req.url.split('/')[3];
    var ret = self.msgs.get(id);
    if(ret) {
      ret = ret.data;
    }
    darkutil.sendJson(resp, ret);
}

function proxy(self, msg) {
    
}

function handle(self, msg) {
    if(msg.data.resp) {
      sendResp(self, msg);
      return;
    }
    proxy(self, msg);
}

function receiveDate(self, req, resp) {
    darkutil.requestLoad(req, resp, (body) => {
        body = JSON.parse(body);
        var saved = self.msgs.get(body.id);
        if(!saved) {
            darkutil.sendNotFound(resp);
            return;
        }
        console.log('request changed', body.id);
        saved.data = body;
        darkutil.sendJson(resp, 'OK');
        handle(self, saved);
    })
}

function darkproxy(self, req, resp) {
    if(req.method == 'GET' && req.url == '/_darkproxy/request') {
        darkutil.sendJson(resp, self.msgs.toList());
    } else if(req.method == 'GET' && req.url.match(/^\/_darkproxy\/request\/[0-9a-fA-F\-]{36}$/)) {
        sendData(self, req, resp);
    } else if(req.method == 'POST' && req.url == '/_darkproxy/request') {
        receiveDate(self, req, resp);
    } else {
        darkutil.sendNotFound(resp);
    }
}

module.exports = darkproxy
