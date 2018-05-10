
const TestCase = require('./TestCase').TestCase;
const assert = require('assert');
const FileHttpServer = require('../src/http/FileHttpServer').TestCase;

class FileHttpServerTest extends TestCase {

  async testServer() {
    let server = new FileHttpServer({ port: 0 });
    await server.start();
    assert.ok(server.port > 0);
    server.stop();
  }

  async testTwice() {
    await this.testServer();
  }

}

exports.FileHttpServerTest = FileHttpServerTest;
