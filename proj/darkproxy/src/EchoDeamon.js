
const net = require('net');

class Connection {

  constructor(conn) {
      this.conn = conn;
  }

}

class EchoDeamon {

  constructor() {

  }

  start(port) {
    if(this.connections) {
      throw 'Server has already started';
    }
    this.connections = [];

    this.server = net.createServer((c) => {
      this._onConnection(c);
    });
    this.server.listen(port, () => {
      console.log(`Listening ${this.server.address().port}`);
    });
  }

  _onConnection(c) {
    this.connections.push(new Connection(c));
    console.log('client connect');
    c.on('error', () => {
      console.log('client error');
    });
    c.on('end', () => {
      console.log('client end');
    });
    c.on('close', () => {
      console.log('client close');
    });
    c.pipe(c);
  }

  stop() {
    console.log('Stopping');
    if(!this.connections) {
        return;
    }
    this.server.close(() => {
      console.log('Stopped');
    });
  }

  kill() {
    this.connections.forEach((c, i) => {
      console.log(`Killing ${i}`);
      c.conn.destroy();
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
