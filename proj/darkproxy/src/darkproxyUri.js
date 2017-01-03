
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

function darkproxy(self, req, resp) {
    if(req.method == 'GET' && req.url == '/_darkproxy/request') {
        sendJson(resp, self.msgs.toList());
    } else {
        sendNotFound(resp);
    }
}

module.exports = darkproxy
