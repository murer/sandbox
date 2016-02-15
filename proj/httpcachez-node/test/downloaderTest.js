var testrunner = require('./testrunner');
var downloader = require('../src/downloader');

testrunner.group('downloaderTest')

testrunner.test('download', function(end) {
  var download = downloader.download('http://repo1.maven.org/maven2/com/googlecode/mycontainer/mycontainer-annotations/1.6.2/mycontainer-annotations-1.6.2.pom.md5')
  download.on('headers', function(resp) {
    testrunner.equal(200, resp.code);
    testrunner.equal('OK', resp.status);
    testrunner.equal(70, resp.headers['Content-Length']);
  });
  download.on('data', function(data) {
    console.log('Received', data.length);
  });
  download.on('end', function() {
    console.log('End');
  });
  download.on('error', function(error) {
    console.log('Error', error);
  });
  download.start();
});
