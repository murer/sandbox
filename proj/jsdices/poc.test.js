const jsdices = require('./jsdices.js');

test('rounding', () => {
  let n5 = 1.5555
  let n6 = 1.5566
  let n4 = 1.5544
  let n = 1

  expect(n5.toFixed(2)).toBe("1.56")
  expect(n6.toFixed(2)).toBe("1.56")
  expect(n4.toFixed(2)).toBe("1.55")
  expect(n.toFixed(2)).toBe("1.00")
});


