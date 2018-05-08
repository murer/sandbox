const http = require('http');

class HttpResponse {

  constructor(resp) {
    this.resp = resp;
  }

}

class HttpClient {

  static request(opts) {
    let client = new HttpClient();
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
