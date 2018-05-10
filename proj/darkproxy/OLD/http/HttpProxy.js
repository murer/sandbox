const http = require('http');
const hc = require('./HttpClient');
const HttpServer = require('./HttpServer').HttpServer;

class HttpProxy extends HttpServer {

  _onRequest(conn) {
    return new Promise(async (resolve, reject) => {
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

      resp.resp.statusCode = clientResp.resp.statusCode;
      resp.resp.statusMessage = clientResp.resp.statusMessage;
      for(let i = 0; i < clientResp.resp.rawHeaders.length; i+=2) {
        let name = clientResp.resp.rawHeaders[i];
        let value = clientResp.resp.rawHeaders[i+1];
        let array = resp.resp.getHeader(name) || [];
        array.push(value);
        resp.resp.setHeader(name, array);
      }
      console.log(`HttpProxy out response: ${resp}`);
      clientResp.resp.pipe(resp.resp);
      resp.resp.once('finish', resolve)
    });
  }

}

const main = async () => {
  let server = new HttpProxy({port: 5000});
  await server.start();
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

exports.HttpProxy = HttpProxy;
