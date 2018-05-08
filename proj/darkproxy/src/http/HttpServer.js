const http = require('http');
const hc = require('./HttpClient');

class HttpServer {

  constructor(opts) {
    this.opts = opts;
  }

  start() {
    return new Promise((resolve, reject) => {
      this.conns = {};
      this.server = http.createServer();
      this.server.listen(this.opts, () => {
        this.server.removeListener('error', reject);
        console.log(`Listening ${this.server.address().port}`);
        resolve();
      });
      this.server.on('error', reject);
      this.server.on('request', async (req, resp) => {
        let conn = hc.HttpConn.from(req, resp);
        if(this.conns[conn.id]) {
          return reject(`connection id failed: ${conn}`);
        }
        this.conns[conn.id] = conn;
        await this._onRequest(conn);
        delete(this.conns[conn.id]);
      });
    });
  }

  async _onRequest(conn) {
    console.log(`HttpServer request: ${conn}`)
    conn.resp.resp.statusCode = 503;
    conn.resp.resp.setHeader('Content-Type', 'text/plain; charset=UTF-8')
    conn.resp.resp.end('Service Unavailable\r\n');
  }

}

const main = async () => {
  let server = new HttpServer({port: 5001});
  await server.start();
}

if (require.main === module) {
  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });
  main(process.argv);
}

exports.HttpServer = HttpServer;
