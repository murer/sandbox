
// const suite = require('./TestCase').suite;
const TestCase = require('./TestCase').TestCase;
const assert = require('assert');

class SimpleTest extends TestCase {

  async testSimple(end) {
    //assert.fail('fail')
  }

  async testTwice(end) {
    await this.testSimple(end);
  }

}

class SimpleTwiceTest extends SimpleTest {
  async testSimple(end) {
  }

  async testThrice(end) {
    await this.testTwice(end);
  }
}

//suite.add(new SimpleTest());
//suite.add(new SimpleTwiceTest());

exports.SimpleTest = SimpleTest;
exports.SimpleTwiceTest = SimpleTwiceTest;
