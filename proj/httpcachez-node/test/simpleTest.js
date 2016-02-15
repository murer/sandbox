var t = require('./testrunner');

t.group('simpleTest')

t.test('test1', function(end) {
  t.ok(1);
  end();
});

t.test('test timeout', 2, function(end) {
  setTimeout(function() {
    t.equal(1, 1);
    t.equal(2, 2);
    end();
  }, 1);
});

t.simple('test2', function() {
  t.ok(1);
});
