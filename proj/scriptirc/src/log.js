var util = require('util')

function stack() {
    var orig = Error.prepareStackTrace;
    Error.prepareStackTrace = function(_, stack) {
        return stack;
    };
    var err = new Error;
    Error.captureStackTrace(err, arguments.callee);
    var stack = err.stack;
    Error.prepareStackTrace = orig;
    return stack[1];
}

function line() {
    return stack().getLineNumber();
}

function func() {
    return stack().getFunctionName();
}

function path() {
    return stack().getFileName();
}

function file() {
    var p = path();
    return p.split('/').pop();
}

function raw(id, name, msgs) {
    var str = util.format('%s [%s] %s:%s:%s %j', 
        new Date().toISOString(),
        name.toUpperCase(),
        file(),
        line(),
        func(), 
        msgs);
    console.log(str);
}

var log = {};
log.raw = raw;
log.func = func;
log.line = line;
log.path = path;

function createLevel(id, name) {
    log[name] = function() {
        var array = Array.prototype.slice.call(arguments);
        log.raw(id, name, array)
    }
}

createLevel(1, 'trace');
createLevel(2, 'debug');
createLevel(3, 'info');
createLevel(4, 'warn');
createLevel(5, 'error');

module.exports = log;
