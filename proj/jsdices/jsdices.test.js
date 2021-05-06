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
  expect(stats.reduce((total, current) => total + current[1], 0)).toBe(50)
  stats.forEach((c, i) => {
    expect(c[0]).toBeGreaterThan(0)
    expect(c[0]).toBeLessThan(9)
    if (i > 0) {
      expect(c[0]).toBeGreaterThan(stats[i-1][0])
    }
    expect(c[1]).toBeGreaterThan(0)
  })
});

test('dataset', () => {
  expect(jsdices.dataset(10, "1", "2", jsdices.parse("1"), "3")).toEqual([[1, 10, 0, 10, 0], [2, 0, 10, 0, 0], [3, 0, 0, 0, 10]])
});

test('round', () => {
  expect(jsdices.parse('1.5').avg()).toBe(1.5)
  expect(jsdices.parse('R(1.5)').avg()).toBe(2)
  expect(jsdices.parse('R(1.4)').avg()).toBe(1)
  expect(jsdices.parse('C(1.2)').avg()).toBe(2)
  expect(jsdices.parse('F(1.9)').avg()).toBe(1)
});


