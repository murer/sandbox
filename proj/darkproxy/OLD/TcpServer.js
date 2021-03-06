const net = require('net');
const util = require('util');
const yld = util.promisify(setImmediate);

class TcpServer {

  static async start(port) {
    return new Promise((resolve, reject) => {
      let ret = new TcpServer();
      ret.state = 'CREATED';
      ret.connections = {};
      ret.server = net.createServer((socket) => {
        ret._onConnected(socket);
      });
      ret.server.on('error', (err) => {
        if(ret.state === 'CREATED') {
          return reject(err);
        }
        console.log('error', ret.server.listening);
      });
      ret.server.listen(port, () => {
        console.log(`Listening ${ret.server.address().port}`);
        ret.state = 'LISTENING';
        resolve(ret);
      });
    });
  }

  _onConnected(socket) {
    let socketId = `${socket.localAddress}:${socket.localPort}-${socket.remoteAddress}:${socket.remotePort}`;
    console.log('client connected', socketId);
    if(this.connections[socketId]) {
      throw 'wrong ' + socketId;
    }
    this.connections[socketId] = socket;
    socket.once('close', () => {
      console.log('client disconnected', socketId);
      delete(this.connections[socketId]);
    });
    socket.pipe(socket);
  }

  async close() {
    return new Promise((resolve, reject) => {
      this.server.close(() => {
        console.log('server closed');
        resolve();
      });
    });
  }

  async waitFor() {
    return new Promise((resolve, reject) => {
      console.log('waitFor')
      this.server.once('close', () => {
        resolve();
      });
    });
  }

  toString() {
    return `${this.socket.localAddress}:${this.socket.localPort}-${this.socket.remoteAddress}:${this.socket.remotePort}`;
  }


}


async function main(args) {

  const sleep = util.promisify(setTimeout);

  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });

  console.log('args', args);

  let server = await TcpServer.start(args[2]);

  setTimeout(() => { server.close(); }, 3000);

  await server.waitFor();

  //await sleep(3000);
  //await server.close();

}

if (require.main === module) {
    main(process.argv);
}

exports.TcpServer = TcpServer;
