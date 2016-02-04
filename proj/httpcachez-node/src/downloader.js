var http = require('http')
var urlparser = require('url')
var mkdirp = require('mkdirp')

function executeDownload(download) {
  var parsed = urlparser.parse(download.url);
  var req = http.request({
    host: parsed.host,
    path: parsed.path,
    port: parsed.port,
    method: 'GET'
  });
  req.on('response', function(resp) {
    resp.on('data', function(chunk) {
      console.log('chunk', chunk);
    });
    resp.on('end', function() {
      console.log('end');
    });
  });
  req.on('error', function(e) {
    console.log('http error', e);
  });
  req.end();
}

function Download(url) {
  (function(download) {
    download.url = url;
    mkdirp('./data', function (err) {
        if (err) {
          throw err;
        }
        executeDownload(download);
    });
  })(this);
}

Download.prototype.done = function(done) {
  this.onDone = done;
}

exports.download = function(url) {
  return new Download(url);
}
