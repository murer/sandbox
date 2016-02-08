var net = require('net');
var testrunner = require('../testrunner');
var replyServer = require('./replyServer');

testrunner.group('replyServerTest')

testrunner.test('server', function(end) {
  replyServer.listen(5000, function(server) {
    console.log('Reply server started on %j', server.server.address());

    var client = new net.Socket();
    client.connect(5000, '127.0.0.1', function() {
      console.log('Client Connected');
      client.write('Test\n');
      client.end();
    });

    var received = '';
    client.on('data', function(data) {
      received += data.toString('utf-8');
    });
    client.on('end', function() {
      console.log('aaa', received)
      testrunner.equal(received, 'Connected\nReply: Test\n');

      server.shutdown(function() {
        end();
      });
    });
  });
});
