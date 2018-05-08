const http = require('http');
const hc = require('./HttpClient');

class HttpProxyConnection {
  constructor(req, resp) {
    this.req = req;
    this.resp = resp;
  }
}

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
      ret.server.on('request', async (req, resp) => {
        let conn = new HttpProxyConnection(new hc.HttpRequest(req), new hc.HttpResponse(resp));
        ret.onRequest(conn);
      });
    });
  }

  async onRequest(conn) {
    let req = conn.req;
    let resp = conn.resp;
    console.log(`HttpProxy in request: ${req}`);
    let target = req.req.headers.host.split(':');
    let opts = {
      hostname: target[0],
      port: target[1] || '80',
      method: req.req.method,
      path: req.req.url
    }

    let clientReq = hc.HttpClient.request(opts);
    console.log(`HttpProxy out request: ${clientReq}`);
    let clientResp = await clientReq.end()
    console.log(`HttpProxy in reponse: ${clientResp}`);

    resp.statusCode = clientResp.resp.statusCode;
    resp.statusMessage = clientResp.resp.statusMessage;
    for(let i = 0; i < clientResp.resp.rawHeaders.length; i+=2) {
      let name = clientResp.resp.rawHeaders[i];
      let value = clientResp.resp.rawHeaders[i+1];
      let array = resp.resp.getHeader(name) || [];
      array.push(value);
      resp.resp.setHeader(name, array);
    }
    console.log(`HttpProxy out response: ${resp}`);
    clientResp.resp.pipe(resp.resp);
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
