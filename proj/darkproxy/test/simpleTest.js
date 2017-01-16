const unit = require('./unittest');

unit.test('simple1', (end) => {
    end();
});

unit.test('simple2', (end) => {
    end();
});

if (require.main === module) {
    unit.execute();
}
