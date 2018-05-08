const suite = require('./TestCase').suite;
const TestCase = require('./TestCase').TestCase;
const assert = require('assert');
const HttpServer = require('../src/http/HttpServer').HttpServer;
const HttpClient = require('../src/http/HttpServer').HttpClient;

class HttpServerTest extends TestCase {

  async testServer() {
    //let server = new HttpServer({port: 0});
    //await server.start();
    throw 2;
  }


}

suite.add(new HttpServerTest());
