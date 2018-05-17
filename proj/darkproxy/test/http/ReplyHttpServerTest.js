
const { TestCase } = require('../TestCase');
const assert = require('assert');
const { ReplyHttpServer } = require('../../src/http/ReplyHttpServer');

class ReplyHttpServerTest extends TestCase {

  async testServer() {
    let server = new ReplyHttpServer({ port: 0 });
    await server.start();
    assert.ok(server.port > 0);
    server.stop();
  }

  async testTwice() {
    await this.testServer();
  }

}

exports.ReplyHttpServerTest = ReplyHttpServerTest;
