var Emitter = require('events');
var http = require('http');
var util = require('util');

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
}

Server.prototype.stop = function() {
  stop(this);
}

Server.prototype.put = function(url, opts) {
  this.contents[url] = opts;
}

exports.server = function(port) {
  return new Server(port);
}

var s = exports.server(5000);
s.on('start', function() {
  console.log('start');
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
