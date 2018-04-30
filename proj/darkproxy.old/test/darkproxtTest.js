const unit = require('./unittest');
const darkproxy = require('../src/darkproxy');

unit.test('darkproxy.requests empty', (end) => {
    var server = new darkproxy.Server();
    server.serve(8000, () => {
        console.log('fjskfkjds')
        server.stop(end);
    });
});

unit.darkproxy('darkproxy.requests empty', (port, end) => {
    console.log('bbbb', port);
    end();
});

if (require.main === module) {
    unit.execute();
}
