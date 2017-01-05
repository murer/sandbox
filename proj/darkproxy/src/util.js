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

function sendError(resp, err) {
    console.log('error on request', err);
    resp.statusCode = 500;
    resp.statusMessage = 'Internal Error';
    resp.removeHeader('Content-Length')
    resp.setHeader('Content-Type', 'text/plain; charset=UTF-8');
    resp.end('' + err);
}

function loadBody(req, resp, success) {
  var body = '';
  req.on('aborted', (err) => {
      sendError(resp, 'client left')
  });
  req.on('data', (data) => {
      console.log('data', data);
      body += data;
  });
  req.on('end', () => {
      success(body);
  });
}

exports.loadBody = loadBody;
exports.sendError = sendError;
exports.sendNotFound = sendNotFound;
exports.sendJson = sendJson;
