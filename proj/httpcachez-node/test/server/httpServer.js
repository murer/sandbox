var Emitter = require('events');
var http = require('http');
var util = require('util');

var single = null;

function send(resp, opts) {
  var content = new Buffer(opts.content.toString('UTF-8'));
  opts.headers['Content-Length'] = content.length
  resp.writeHead(opts.code, opts.message, opts.headers);
  resp.end(opts.content)
}

function handleRequest(server) {
  return function(req, resp) {
    var opts = server.contents[req.url];
    if(!opts) {
      return send(resp, {
        code: 404,
        message: 'Not Found',
        headers: {
          'Content-Type': 'text/plain; charset=UTF-8'
        },
        content: 'Not found: ' + req.url + '\n'
      });
    }
    return send(resp, opts);
  }
}

function Server(port) {
  Emitter.call(this);
  this.port = port;
  this.contents = {};
  this.server = http.createServer(handleRequest(this));
}
util.inherits(Server, Emitter);

function start(server) {
  server.server.listen(server.port, function() {
    server.port = server.server.address().port;
    server.emit('start');
  });
}

function stop(server) {
  server.server.close(function() {
    server.emit('stop');
  });
}

Server.prototype.start = function() {
  start(this);
  return this;
}

Server.prototype.stop = function() {
  stop(this);
  return this;
}

Server.prototype.put = function(url, opts) {
  this.contents[url] = opts;
}

exports.server = function(port) {
  return new Server(port);
}

exports.initSingle = function(port) {
  single = new Server(port);
  return single;
}

exports.single = function(port) {
  return single;
}

/**
var s = exports.server(0);
s.on('start', function() {
  console.log('start', s.port);
});
s.on('stop', function() {
  console.log('stop');
});
s.start();
s.put('/ping.txt', {
  code: 200,
  message: 'OK',
  headers: {
    'X-Any': 'abc',
    'X-Other': 'other',
    'Content-Type': 'text/plain; charset=UTF-8'
  },
  content: 'PONG'
});
setTimeout(function() {
  s.stop();
}, 5000)
*/
