var testrunner = require('./testrunner');
var downloader = require('../src/downloader');

testrunner.group('downloaderTest')

testrunner.test('download', 1, function(end) {
  var download = downloader.download('http://repo1.maven.org/maven2/com/googlecode/mycontainer/mycontainer-annotations/1.6.2/mycontainer-annotations-1.6.2.pom.md5')
  var buffer = new Buffer(0);
  download.on('response', function(resp) {
    testrunner.equal(200, resp.code);
    testrunner.equal('OK', resp.message);
    testrunner.equal(32, resp.headers['Content-Length']);
  });
  download.on('data', function(data) {
    buffer = Buffer.concat([buffer, data]);
  });
  download.on('end', function() {
    testrunner.equal('35021fb13df866d2df394f5d7b169abe', buffer.toString('utf-8'));
    end();
  });
  download.on('error', function(error) {
    testrunner.fail(error)
    end();
  });
  download.start();
});
