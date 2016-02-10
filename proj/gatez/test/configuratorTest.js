var testrunner = require('./testrunner');
var configurator = require('../src/configurator');

testrunner.group('configuratorTest')

testrunner.test('config', function(end) {
  var names = [];
  configurator.all(function(configs) {
    testrunner.equal(configs['test'].name, 'test');
    testrunner.equal(configs['local'].name, 'local');
    end();
  });
});
