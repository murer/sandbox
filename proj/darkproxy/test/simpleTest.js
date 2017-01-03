const unit = require('./unittest');

unit.test('simple', (end) => {
    end();
});

if (require.main === module) {
    unit.execute();
}
