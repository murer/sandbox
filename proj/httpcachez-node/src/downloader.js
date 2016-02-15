var Emitter = require('events');
var util = require('util');
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
  Emitter.call(this);
  this.url = url;
  this.parsed = urlparser.parse(url);
  var port = this.parsed.port || 80;
  this.path = './data/' + this.parsed.host + '/' + port;
}
util.inherits(Download, Emitter);

Download.prototype.start = function() {

}

exports.download = function(url) {
  return new Download(url);
}
