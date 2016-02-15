var testrunner = require('./testrunner');

require('./simpleTest');
require('./server/replyServerTest');
require('./downloaderTest');

//testrunner.onError = function(error) {
//  throw 'ERROR: ' + this.current.name + ' ' + error.join('');
//};

//testrunner.onTestStarted = function(end) {
//  console.log('Test Started: ' + this.current.ident());
//}


testrunner.execute();
