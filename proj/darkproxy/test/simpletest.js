
const suite = require('./testcase').suite;
const TestCase = require('./testcase').TestCase;
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
