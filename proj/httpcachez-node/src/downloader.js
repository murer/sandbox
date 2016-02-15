var http = require('http')
var urlparser = require('url')
var mkdirp = require('mkdirpd')

function executeDownload(download) {
  var req = http.request({
    host: download.parsed.host,
    path: download.parsed.path,
    port: download.parsed.port,
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
    download.parsed = urlparser.parse(url);
    var port = download.parsed.port || 80;
    download.path = './data/' + download.parsed.host + '/' + port;
    console.log('creating', download.path)
    mkdirp(download.path, function (err) {
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
