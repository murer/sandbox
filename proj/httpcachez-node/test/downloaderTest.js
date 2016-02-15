var t = require('./testrunner');
var server = require('./server/httpServer');
var downloader = require('../src/downloader');

t.group('downloaderTest')

t.test('downloadHttpOk', 4, function(end) {
  var download = downloader.download('http://localhost:' + server.single().port + '/ping.txt')
  var buffer = new Buffer(0);
  download.on('response', function(resp) {
    t.equal(200, resp.code);
    t.equal('OK', resp.message);
    t.equal(4, resp.headers['Content-Length']);
  });
  download.on('data', function(data) {
    buffer = Buffer.concat([buffer, data]);
  });
  download.on('end', function() {
    t.equal('PONG', buffer.toString('utf-8'));
    end();
  });
  download.on('error', function(error) {
    t.fail(error)
    end();
  });
  download.start();
});

t.test('downloadHttpNotFound', 2, function(end) {
  var download = downloader.download('http://localhost:' + server.single().port + '/not-found.txt')
  var buffer = new Buffer(0);
  download.on('response', function(resp) {
    t.equal(404, resp.code);
    t.equal('Not Found', resp.message);
  });
  download.on('end', function() {
    end();
  });
  download.on('error', function(error) {
    t.fail(error)
    end();
  });
  download.start();
});

t.test('downloadHttpError', 1, function(end) {
  var download = downloader.download('http://localhost-not-found:' + server.single().port + '/ping.txt')
  var buffer = new Buffer(0);
  download.on('response', function(resp) {
    t.fail('it should fail');
  });
  download.on('end', function() {
    t.fail('it should fail');
    end();
  });
  download.on('error', function(error) {
    t.ok('ENOTFOUND', error.code);
    end();
  });
  download.start();
});
