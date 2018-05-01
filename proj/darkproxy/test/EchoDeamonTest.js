const suite = require('./testcase').suite;
const TestCase = require('./testcase').TestCase;
const assert = require('assert');
const EchoDeamin = require('../src/Deamon').EchoDeamon;

class EchoDeamonTest extends TestCase {

  testSimple(end) {
    //assert.fail('fail')
    end();
  }

  testTwice(end) {
    this.testSimple(end);
  }

}

suite.add(new EchoDeamonTest());
