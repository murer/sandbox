const darkutil = require('./util');

function sendData(self, req, resp) {
    var id = req.url.split('/')[3];
    var ret = self.msgs.get(id);
    if(ret) {
      ret = ret.data;
    }
    darkutil.sendJson(resp, ret);
}

function receiveDate(self, req, resp) {
    darkutil.requestLoad(req, resp, (body) => {
        console.log('darkproxy load', body);
    })
}

function darkproxy(self, req, resp) {
    if(req.method == 'GET' && req.url == '/_darkproxy/request') {
        darkutil.sendJson(resp, self.msgs.toList());
    } else if(req.method == 'GET' && req.url.match(/^\/_darkproxy\/request\/[0-9a-fA-F\-]{36}$/)) {
        sendData(self, req, resp);
    } else if(req.method == 'POST' && req.url.match(/^\/_darkproxy\/request\/[0-9a-fA-F\-]{36}$/)) {
        receiveDate(self, req, resp);
    } else {
        darkutil.sendNotFound(resp);
    }
}

module.exports = darkproxy
