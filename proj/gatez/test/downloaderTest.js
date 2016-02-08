var testrunner = require('./testrunner');
var downloader = require('../src/downloader');

testrunner.group('downloaderTest')

testrunner.test('download', function(end) {
  var download = downloader.download('http://repo1.maven.org/maven2/com/googlecode/mycontainer/mycontainer-annotations/1.6.2/mycontainer-annotations-1.6.2.pom.md5')
  download.done(function() {
    end();
  });
});
