const jsdices = require('./jsdices.js');

test('poc', () => {

  let roll = jsdices.parse('2d6+?+4+?-${a}+${bb}+3', { a: 5, bb: '2d12' }, '1d8', '9')

  expect(roll.toString()).toBe("aaa")
  expect("a: " + roll).toBe("a: aaa")

  expect(roll.avg()).toBe(1)

  expect(roll.rnd()).toBeGreaterThan(0)
});

