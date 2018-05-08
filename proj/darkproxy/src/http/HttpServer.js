const http = require('http');
const hc = require('./HttpClient');

class HttpServer {

  static start(opts) {
    return new Promise((resolve, reject) => {
      let ret = new HttpServer();
      ret.conns = {};
      ret.server = http.createServer();
      ret.server.listen(opts, () => {
        ret.server.removeListener('error', reject);
        resolve(ret);
      });
      ret.server.on('error', reject);
      ret.server.on('request', async (req, resp) => {
        let conn = hc.HttpConn.from(req, resp);
        if(ret.conns[conn.id]) {
          return reject(`connection id failed: ${conn}`);
        }
        ret.conns[conn.id] = conn;
        await ret._onRequest(conn);
        delete(ret.conns[conn.id]);
      });
    });
  }

  async _onRequest(conn) {
    console.log(`Reimplement _onRequest: ${conn}`)
    conn.resp.resp.statusCode = 503;
    conn.resp.resp.setHeader('Content-Type', 'text/plain; charset=UTF-8')
    conn.resp.resp.end('Implement it\r\n');
  }

}

const main = async () => {
  let proxy = await HttpServer.start({port: 5000});
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

exports.HttpServer = HttpServer;
