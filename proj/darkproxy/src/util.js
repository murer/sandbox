
function sendError(resp, err) {
    console.log('error on request', err);
    resp.statusCode = 500;
    resp.statusMessage = 'Internal Error';
    resp.removeHeader('Content-Length')
    resp.setHeader('Content-Type', 'text/plain; charset=UTF-8');
    resp.end('' + err);
}

function requestLoad(req, resp, success) {
  var body = '';
  req.on('aborted', (err) => {
      sendError(resp, 'client left')
  });
  req.on('data', (data) => {
      body += data;
  });
  req.on('end', () => {
      success(body);
  });
}

exports.requestLoad = requestLoad;
exports.sendError = sendError;
