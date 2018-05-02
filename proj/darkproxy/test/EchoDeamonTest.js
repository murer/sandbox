const suite = require('./testcase').suite;
const TestCase = require('./testcase').TestCase;
const assert = require('assert');
const EchoDeamon = require('../src/EchoDeamon').EchoDeamon;
const net = require('net');

class EchoDeamonTest extends TestCase {

  testDeamon(end) {
    let deamon = new EchoDeamon();
    deamon.start(0).then((port) => {
      assert.ok(port > 1024);
      let client = net.createConnection(port, 'localhost', (c) => {
        client.end('test');
      });
      client.on('data', (data) => {
        assert.equal(data, 'test');
        deamon.stop().then(() => {
          end();
        });
      });
    });
  }


}

suite.add(new EchoDeamonTest());
