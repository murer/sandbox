
function sendJson(resp, obj) {
    resp.statusCode = 200;
    resp.statusMessage = 'OK';
    resp.setHeader('Content-Type', 'application/json; charset=UTF-8');
    resp.end(JSON.stringify(obj, null, 4));
}

function sendNotFound(resp) {
    resp.statusCode = 404;
    resp.statusMessage = 'Not Found';
    resp.setHeader('Content-Type', 'text/plain; charset=UTF-8');
    resp.end('Not Found');
}

function sendData(self, req, resp) {
    var id = req.url.split('/')[3];
    var ret = self.msgs.get(id);
    if(ret) {
      ret = ret.data;
    }
    sendJson(resp, ret);
}

function darkproxy(self, req, resp) {
    if(req.method == 'GET' && req.url == '/_darkproxy/request') {
        sendJson(resp, self.msgs.toList());
    } else if(req.method == 'GET' && req.url.match(/^\/_darkproxy\/request\/[0-9a-fA-F\-]{36}$/)) {
        sendData(self, req, resp);
    } else {
        sendNotFound(resp);
    }
}

module.exports = darkproxy
