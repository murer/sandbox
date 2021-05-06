const jsdices = require('./jsdices.js');

test('rollDice', () => {
  expect(jsdices.rollDice(6, 2)).toBeGreaterThan(0)
  expect(jsdices.rollDice(6, 2)).toBeLessThan(13)
  expect(jsdices.rollDice(1)).toBeLessThan(2)
});

test('basics', () => {
  let roll = jsdices.parse('2d6+?+4+?-${a}+${bb}+3', { a: 5, bb: '2d12' }, '1d8', '9')
  expect(roll.toString()).toBe("2d6+1d8+4+9-5+2d12+3")
  expect("a: " + roll).toBe("a: 2d6+1d8+4+9-5+2d12+3")
  expect(roll.avg()).toBe(35.5)
  expect(roll.rnd()).toBeGreaterThan(0)
});

test('stats', () => {
  expect(jsdices.parse('3').stats(100)).toEqual([[3, 100]])

  let stats = jsdices.parse('2d4').stats(50)
  console.log('xxx', stats)
  expect(stats.reduce((total, current) => total + current, 0)).toBe(50)
});


