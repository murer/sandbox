const process = require('process');

var fns = [];
function test(name, fn) {
    fns.push({
        name: name,
        fn: fn
    });
}

function executeOne(proc, callback) {
    console.log('Test', proc.name);
    proc.fn(callback);
}

function execute() {
    var procs = [].concat(fns);
    var error = null;

    console.log('Tests', procs.length);
    function executeNext() {
        if(error) {
            console.log('ERROR');
            process.exit(1);
            return;
        }
        if(!procs.length) {
            console.log('SUCCESS');
            return;
        }
        var proc = procs.shift();
        executeOne(proc, (err) => {
            error = err;
            executeNext();
        });
    }

    executeNext(procs);
}

exports.test = test;
exports.execute = execute;
