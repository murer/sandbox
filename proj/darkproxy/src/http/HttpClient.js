const http = require('http');

class HttpConn {

  constructor(req, resp) {
    this.req = req;
    this.resp = resp;
    this.id = `${this.req.req.socket.localAddress}:${this.req.req.socket.localPort}-${this.req.req.socket.remoteAddress}:${this.req.req.socket.remotePort}`
  }

  static from(req, resp) {
    return new HttpConn(new HttpRequest(req), new HttpResponse(resp));
  }

  toString() {
    return `${this.id} ${this.req} ${this.resp}`;
  }

}

class HttpRequest {

  constructor(req) {
    this.req = req;
  }

  toString() {
    return `${this.req.method} ${this.req.headers.host} ${this.req.url}`;
  }

}

class HttpResponse {

  constructor(resp) {
    this.resp = resp;
  }

  toString() {
    return `HTTP/${this.resp.httpVersion} ${this.resp.statusCode} ${this.statusMessage}`;
  }

}

class HttpClient {

  static request(opts) {
    let client = new HttpClient();
    client.opts = opts;
    client.req = http.request(opts);
    return client;
  }

  end(data) {
    return new Promise((resolve, reject) => {
      this.req.once('response', (resp) => {
        resolve(new HttpResponse(resp));
      });
      this.req.end(data);
    });
  }

  toString() {
    return `${this.opts.method} ${this.opts.hostname}:${this.opts.port} ${this.opts.path}`;
  }

}


const main = async () => {
  let client = HttpClient.request({port: 80, hostname: 'google.com' });
  let resp = await client.end();
  console.log('rawHeaders', resp.resp.rawHeaders);
  console.log('headers', resp.resp.headers);
  resp.resp.pipe(process.stdout);
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

exports.HttpRequest = HttpRequest;
exports.HttpResponse = HttpResponse;
exports.HttpConn = HttpConn;
exports.HttpClient = HttpClient;
