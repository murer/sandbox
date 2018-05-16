const TestCase = require('../TestCase').TestCase;
const assert = require('assert');

const { MemoryReadable } = require('./MemoryStream');

class MemoryReadableTest extends TestCase {

  testRead() {
    return new Promise((resolve, reject) => {
      resolve();
    });
  }

}

exports.MemoryReadableTest = MemoryReadableTest;
