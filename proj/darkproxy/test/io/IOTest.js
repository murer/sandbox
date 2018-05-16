const TestCase = require('../TestCase').TestCase;
const assert = require('assert');

const { MemoryReadable, MemoryWritable } = require('./MemoryStream');
const { AsyncReadable } = require('../../src/io/AsyncStream');

class MemoryWritableTest extends TestCase {

  testWrite() {
    return new Promise((resolve, reject) => {
      let output = new MemoryWritable();
      output.write('aa', () => {
        output.write('bb', () => {
          output.end('cc', () => {
            let result = output.chunks.map(chunk => chunk.toString('utf8'));
            assert.deepEqual(result, ['aa', 'bb', 'cc']);
            resolve();
          });
        });
      });
    });
  }

  testError() {
    return new Promise((resolve, reject) => {
      let output = new MemoryWritable();
      output.on('error', (err) => {
        assert.equal(err.message, 'failed');
        resolve();
      });
      output.write('aa', () => {
        output.write('bb', () => {
          let result = output.chunks.map(chunk => chunk.toString('utf8'));
          assert.deepEqual(result, ['aa', 'bb' ]);
          output.err = new Error('failed');
          output.write('cc', (err) => {
            assert.equal(err.message, 'failed');
          });
        });
      });
    });
  }

}

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

  testError() {
    return new Promise((resolve, reject) => {

      let input = new MemoryReadable('aa', 'bb', null);
      let result = [];
      input.on('data', (data) => {
        result.push(data);
      });
      input.on('error', (err) => {
        assert.equal(err.message, 'failed');
        resolve(err);
      });
      input.on('end', () => {
        assert.fail('error expected');
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

  async testError() {
    let input = new AsyncReadable(new MemoryReadable('aa', 'bb', null));
    try {
      assert.equal(await input.read(), 'aa');
      assert.equal(await input.read(), 'bb');
      await input.read();
    } catch (err) {
      assert.equal(err.message, 'failed');
    }
  }

}

exports.MemoryReadableTest = MemoryReadableTest;
exports.AsyncReadableTest = AsyncReadableTest;
exports.MemoryWritableTest = MemoryWritableTest;
