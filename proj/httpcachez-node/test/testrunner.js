
function TestRunner() {
  this.tests = [];
}

function processNext(runner) {
  if(!runner.jobs.length) {
    runner.finishedAt = new Date().getTime();
    runner.time = runner.finishedAt - runner.startedAt;
    runner.onFinished();
    return;
  }
  runner.current = runner.jobs.shift();
  runner.current.startedAt = new Date().getTime();

  runner.timeout = setTimeout(function() {
    runner.fail('Timeout')
  }, 1000);

  runner.onTestStarted(function() {
    runner.current.func(function() {
      runner.current.finishedAt = new Date().getTime();
      clearTimeout(runner.timeout);
      runner.timeout = null;
      runner.current.time = runner.current.finishedAt - runner.current.startedAt;
      runner.onTestFinished(function() {
        processNext(runner);
      });
    });
  });
}

TestRunner.prototype.group = function(name) {
  this.module = name;
}

TestRunner.prototype.test = function addTest(name, func) {
  this.tests.push({
    module: this.module,
    name: name,
    func: func,
    errors: [],
    ident: function() {
      return '' + this.module + '.' + this.name;
    }
  });
}

TestRunner.prototype.simple = function(name, func) {
  this.test(name, function(end) {
    func();
    end();
  });
}

TestRunner.prototype.execute = function execute() {
  this.startedAt = new Date().getTime();
  this.jobs = [].concat(this.tests);
  processNext(this);
}

TestRunner.prototype.onTestStarted = function(end) {
  console.log('Test Started: ' + this.current.ident());
  end();
}

TestRunner.prototype.onTestFinished = function(end) {
  console.log('Test Finished: ' + this.current.ident() + ' ' + this.current.time + ' millis');
  end();
}

TestRunner.prototype.onFinished = function() {
  console.log('TEST DONE, time: ' + this.time + ' millis');
  var fail = false;
  for(var i = 0; i < this.tests.length; i++) {
    var entry = this.tests[i];
    var msg = 'TEST SUMMARY ' + entry.ident() + ': ';
    if(entry.errors.length) {
      fail = true;
      console.log(msg + 'FAIL, errors: ' + entry.errors.length + ', time: ' + entry.time + ' millis')
    } else {
      console.log(msg + 'SUCCESS, time: ' + entry.time + ' millis')
    }
  }
  console.log('TEST ' + (fail?'FAIL':'SUCCESS'));
  if(fail) {
    throw 'FAILED'
  }
}

TestRunner.prototype.onError = function(error) {
  console.error.apply(this, ['ERROR', '[' + this.current.ident()  + ']'].concat(error));
}

TestRunner.prototype.error = function(msg) {
  this.current.errors.push(msg);
  this.onError(msg)
}

TestRunner.prototype.ok = function(a, msg) {
  a || this.error(msg);
}

TestRunner.prototype.fail = function(msg) {
  this.error(msg);
}

TestRunner.prototype.equal = function(a, b, msg) {
  this.ok(a == b, ['expected ', a, ', but was: ', b].concat(msg));
}

module.exports = new TestRunner();
