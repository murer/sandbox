
const suite = require('./TestCase').suite;
const TestCase = require('./TestCase').TestCase;
const assert = require('assert');

class SimpleTest extends TestCase {

  testSimple(end) {
    //assert.fail('fail')
    end();
  }

  testTwice(end) {
    this.testSimple(end);
  }

}

class SimpleTwiceTest extends SimpleTest {
  testSimple(end) {
    //assert.fail('fail')
    end();
  }

  testThrice(end) {
    this.testTwice(end);
  }
}

suite.add(new SimpleTest());
suite.add(new SimpleTwiceTest());
