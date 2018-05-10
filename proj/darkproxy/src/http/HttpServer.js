const LOG = require('../Logger.js')(__filename);
const http = require('http');

class HttpServer {

  constructor(params) {
    this.params = params;
  }

  toString() {
    return `[${this.constructor.name} ${this.port}]`;
  }

  start() {
    return new Promise((resolve, reject) => {
      this.conns = {};
      this.server = http.createServer();
      this.server.listen(this.params, () => {
        this.server.removeListener('error', reject);
        this.port = this.server.address().port;
        LOG.info(`${this} started`);
        resolve();
      });
      this.server.on('error', reject);
      this.server.on('request', (req, resp) => {
      });
    });
  }

  stop() {
    return new Promise((resolve, reject) => {
      this.server.close(() => {
        LOG.info(`${this} stopped`);
        resolve();
      });
    });
  }

}

exports.HttpServer = HttpServer;
