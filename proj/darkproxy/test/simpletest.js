
const suite = require('./testcase').suite;
const TestCase = require('./testcase').TestCase;

class SimpleTest extends TestCase {

  testSimple(end) {
    end();
  }

  testTwice(end) {
    testSimple(end);
  }

}

suite.add(new SimpleTest());
