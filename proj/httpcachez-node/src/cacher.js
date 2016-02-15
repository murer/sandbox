var Emitter = require('events');
var util = require('util');
var downloader = require('./downloader')

function Cache(url) {
  Emitter.call(this);
  this.downloader = downloader.download(url);
  var port = this.downloader.parsed.port || 80;
  this.path = './data/' + this.downloader.parsed.hostname + '/' + port + this.downloader.parsed.path;
  this.contentPath = this.path + '/__cachez_content';
  this.headersPath = this.path + '/__cachez_headers';
  
}
util.inherits(Cache, Emitter);

Cache.prototype.start = function() {

}

exports.download = function(url) {
  return new Cache(url);
}
