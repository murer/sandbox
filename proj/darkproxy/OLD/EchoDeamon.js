
const net = require('net');

class EchoConnection {

  constructor(conn) {
      this.conn = conn;
      console.log('client connect');
      this.conn.on('error', () => {
        console.log('client error');
      });
      this.conn.on('end', () => {
        console.log('client end');
      });
      this.conn.on('close', () => {
        console.log('client close');
      });
      this.conn.pipe(this.conn);
  }

  destroy() {
    this.conn.destroy();
  }

}

class EchoDeamon {

  constructor() {

  }

  start(port) {
    if(this.connections) {
      throw 'Server has already started';
    }
    this.connections = {};

    this.server = net.createServer((c) => {
      this._onConnection(c);
    });
    return new Promise((r, e) => {
      this.server.listen(port, () => {
        let localPort = this.server.address().port;
        console.log(`Listening ${localPort}`);
        r(localPort);
      });
    });
  }

  _onConnection(c) {
    this.connections.push(new EchoConnection(c));
  }

  stop() {
    console.log('Stopping');
    if(!this.connections) {
        return;
    }
    return new Promise((r, e) => {
      this.server.close(() => {
        console.log('Stopped');
        r();
      });
    });
  }

  kill() {
    this.connections.forEach((c, i) => {
      console.log(`Killing ${i}`);
      c.destroy();
    });
  }

}

function main() {
  let deamon = new EchoDeamon();
  deamon.start(5005);
  setTimeout(() => {
    deamon.stop();
  }, 5000);
  setTimeout(() => {
    deamon.kill();
  }, 10000);
}

if (require.main === module) {
    main();
}

exports.EchoDeamon = EchoDeamon;
