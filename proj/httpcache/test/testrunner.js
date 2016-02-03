
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
  runner.onTestStarted(function() {
    runner.current.func(function() {
      runner.current.finishedAt = new Date().getTime();
      runner.current.time = runner.current.finishedAt - runner.current.startedAt;
      runner.onTestFinished(function() {
        processNext(runner);
      });
    });
  });
}

function concat(target) {
  var ret = [];
  for(var i = 0; i < arguments.length; i++) {
    var array =  arguments[i];
    for(var j = 0; j < array.length; j++) {
      console.log('x', i, j, array.length)
      ret.push(array[j])
    }
  }
  return ret;
}

function toArray(array) {
  var ret = [];
  for(var i = 0; i < array.length; i++) {
    ret.push(array[i]);
  }
  return ret;
}

TestRunner.prototype.test = function addTest(name, func) {
  this.tests.push({
    name: name,
    func: func,
    errors: []
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
  console.log('Test Started: ' + this.current.name);
  end();
}

TestRunner.prototype.onTestFinished = function(end) {
  console.log('Test Finished: ' + this.current.name + ' ' + this.current.time + ' millis');
  end();
}

TestRunner.prototype.onFinished = function() {
  console.log('TEST DONE, time: ' + this.time + ' millis');
  var fail = false;
  for(var i = 0; i < this.tests.length; i++) {
    var entry = this.tests[i];
    var msg = 'TEST SUMMARY ' + entry.name + ': ';
    if(entry.errors.length) {
      fail = true;
      console.log(msg + 'FAIL, errors: ' + entry.errors.length + ', time: ' + entry.time + ' millis')
    } else {
      console.log(msg + 'SUCCESS, time: ' + entry.time + ' millis')
    }
  }
  console.log('TEST ' + (fail?'FAIL':'SUCCESS'));
}

TestRunner.prototype.onError = function(error) {
  console.error.apply(this, ['ERROR', '[' + this.current.name  + ']'].concat(error));
}

TestRunner.prototype.error = function(msg) {
  this.current.errors.push(msg);
  this.onError(msg)
}

TestRunner.prototype.ok = function(a, msg) {
  a || this.error(msg);
}

TestRunner.prototype.equal = function(a, b, msg) {
  this.ok(a == b, ['expected ', a, ', but was: ', b].concat(msg));
}

module.exports = new TestRunner();
