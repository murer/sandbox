var testrunner = require('./testrunner');

testrunner.group('simpleTest')

testrunner.test('test1', function(end) {
  testrunner.ok(1);
  end();
});

testrunner.test('test timeout', 2, function(end) {
  setTimeout(function() {
    testrunner.equal(1, 1);
    testrunner.equal(2, 2);
    end();
  }, 1);
});

testrunner.simple('test2', function() {
  testrunner.ok(1);
});
