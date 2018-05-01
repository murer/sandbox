const suite = require('./testcase').suite;
const TestCase = require('./testcase').TestCase;
const assert = require('assert');
const EchoDeamon = require('../src/EchoDeamon').EchoDeamon;

class EchoDeamonTest extends TestCase {

  testDeamon(end) {
    let deamon = new EchoDeamon();
    deamon.start(0).then((port) => {
      assert.ok(port > 1024);
      deamon.stop().then(() => {
        end();
      }).catch((err) => {
        assert.fail(err);
      });
    }).catch((err) => {
      assert.fail(err);
    });
  }


}

suite.add(new EchoDeamonTest());
