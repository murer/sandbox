var testrunner = require('./testrunner');
var mkdirp = require('mkdirpd');

require('./simpleTest')
require('./downloaderTest')

//testrunner.onError = function(error) {
//  throw 'ERROR: ' + this.current.name + ' ' + error.join('');
//};

testrunner.onTestStarted = function(end) {
  console.log('Test Started: ' + this.current.ident());
  mkdirp.delete('./data', function(err) {
    testrunner.ok(!err, err);
    console.log('xxxx');
  });

}


testrunner.execute();
