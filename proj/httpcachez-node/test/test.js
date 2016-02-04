var testrunner = require('./testrunner');

require('./simpleTest')
require('./downloaderTest')

//testrunner.onError = function(error) {
//  throw 'ERROR: ' + this.current.name + ' ' + error.join('');
//};


testrunner.execute();
