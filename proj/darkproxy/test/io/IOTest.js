const TestCase = require('../TestCase').TestCase;
const assert = require('assert');

const { MemoryReadable } = require('./MemoryStream');
const { AsyncReadable } = require('../../src/io/AsyncStream');

class MemoryReadableTest extends TestCase {

  testRead() {
    return new Promise((resolve, reject) => {

      let input = new MemoryReadable('aa', 'bb', 'cc');
      let result = [];
      input.on('data', (data) => {
        result.push(data);
      });
      input.on('error', (err) => {
        reject(err);
      });
      input.on('end', () => {
        assert.equal(result[0], 'aa');
        assert.equal(result[1], 'bb');
        assert.equal(result[2], 'cc');
        assert.equal(result.length, 3);
        resolve();
      });
    });
  }

}

class AsyncReadableTest extends TestCase {

  async testRead() {
    let input = new AsyncReadable(new MemoryReadable('aa', 'bb', 'cc'));
    assert.equal(await input.read(), 'aa');
    assert.equal(await input.read(), 'bb');
    assert.equal(await input.read(), 'cc');
    assert.equal(await input.read(), null);
    assert.equal(await input.read(), null);
  }

}

exports.MemoryReadableTest = MemoryReadableTest;
exports.AsyncReadableTest = AsyncReadableTest;
