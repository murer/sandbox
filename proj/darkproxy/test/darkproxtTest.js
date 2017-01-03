const unit = require('./unittest');
const darkproxy = require('../src/darkproxy');

unit.test('darkproxy.requests empty', (end) => {
    var server = new darkproxy.Server();
    server.serve();
    server.stop(end);
});

if (require.main === module) {
    unit.execute();
}
