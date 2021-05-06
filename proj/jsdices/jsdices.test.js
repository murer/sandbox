const jsdices = require('./jsdices.js');

test('poc', () => {

  let roll = jsdices.parse('2d6+?+4+?-$a+$b', { a: 5, b: '2d12' }, '1d8', '9')

  // expect(sum(1, 2)).toBe(3);
});

