var http = require('./server/httpServer');
var t = require('./testrunner');

require('./simpleTest');
require('./server/replyServerTest');
require('./downloaderTest');

//t.onError = function(error) {
//  throw 'ERROR: ' + this.current.name + ' ' + error.join('');
//};

t.onTestStarted = function(end) {
  console.log('Test Started: ' + this.current.ident());
  http.initSingle(0).on('start', function() {
    end();
  }).start();
}

t.onTestFinished = function(end) {
  http.single().on('stop', function() {
    end();
  }).stop();
}


t.execute();
