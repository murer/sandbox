const http = require('http');

class HttpProxy {

  static start(opts) {
    return new Promise((resolve, reject) => {
      let ret = new HttpProxy();
      ret.server = http.createServer();
      ret.server.listen(opts, () => {
        ret.server.removeListener('error', reject);
        resolve(ret);
      });
      ret.server.on('error', reject);
      ret.server.on('request', (req, resp) => {
        ret.onRequest(req, resp);
      });
    });
  }

  async onRequest(req, resp) {
    resp.statusCode = 200;
    resp.end('OK');
  }

}

const main = async () => {
  let proxy = await HttpProxy.start({port: 5000});
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

exports.HttpProxy = HttpProxy;
