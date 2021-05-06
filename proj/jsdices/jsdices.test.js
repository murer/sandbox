const jsdices = require('./jsdices.js');

test('poc', () => {

  let roll = jsdices.parse('2d6+?+4+?-$a+$b', { a: 5, b: '2d12' }, '1d8', '9')

  expect(roll.avg()).toBe(1)

  expect(roll.rnd()).toBeGreaterThan(0)
});

