var http = require('./server/httpServer');
var t = require('./testrunner');

require('./simpleTest');
require('./downloaderTest');

require('./downloaderIT');

//t.onError = function(error) {
//  throw 'ERROR: ' + this.current.name + ' ' + error.join('');
//};

t.onTestStarted = function(end) {
  console.log('Test Started: ' + this.current.ident());
  var server = http.initSingle(0).on('start', function() {
    end();
  }).start();
  server.put('/ping.txt', {
    code: 200,
    message: 'OK',
    headers: {
      'X-Any': 'abc',
      'X-Other': 'other',
      'Content-Type': 'text/plain; charset=UTF-8'
    },
    content: 'PONG'
  });
}

t.onTestFinished = function(end) {
  http.single().on('stop', function() {
    end();
  }).stop();
}


t.execute();
