
const { TestCase } = require('./TestCase');
const assert = require('assert');

class SimpleTest extends TestCase {

  async testSimple() {
    //assert.fail('fail')
  }

  async testTwice() {
    await this.testSimple();
  }

}

class SimpleTwiceTest extends SimpleTest {

  async testSimple() {
  }

  async testThrice() {
    await this.testTwice();
  }
}

exports.SimpleTest = SimpleTest;
exports.SimpleTwiceTest = SimpleTwiceTest;
