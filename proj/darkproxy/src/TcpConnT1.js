const net = require('net');
const util = require('util');

class TcpConn {
  static connect(host, port) {
    return new Promise((resolve, reject) => {
      let ret = new TcpConn();
      ret.buffer = Buffer.from([]);
      ret.state = 'CREATED';
      ret.maxBufferSize = 100;
      ret.socket = net.connect(port, host, () => {
        ret.status = 'CONNECTED';
        resolve(ret);
      });

      ret.socket.on('error', (err) => {
        if(ret.state === 'CREATED') {
          return reject(err);
        }
        console.log('error', err);
      });

      ret.socket.on('end', () => {
        console.log('connection end');
      });

      ret.socket.on('close', (hasError) => {
        console.log('connection close', hasError);
        ret.state = 'CLOSED';
      });

      ret.socket.once('data', (data) => {
        console.log('internal data', data.length, ret.buffer.length + data.length);
        ret.buffer = Buffer.concat([ret.buffer, data]);
        if(ret.buffer.length >= ret.maxBufferSize) {
          console.log('pause')
          //ret.socket.pause();
        }
      });

    });
  }

  async write(data) {
    return new Promise((resolve, reject) => {
      console.log('write', data);
      this.socket.write(data);
      setTimeout(() => {
        resolve();
      }, 500);
    });
  }

  async read(max) {
    return new Promise((resolve, reject) => {
      if(max === 0) {
        return resolve(Buffer.from([]));
      }
      max = Math.min(this.buffer.length, max);
      console.log('bufferSize', this.socket.bufferSize, this.buffer.length);
      if(max === 0 && !this.socket.bufferSize && this.state === 'CLOSED') {
        resolve(null);
        return;
      }
      if(max === 0) {
        setTimeout(() => {
          resolve(Buffer.from([]));
        }, 500);
        return;
      }
      let ret = this.buffer.slice(0, max);
      this.buffer = this.buffer.slice(max, this.buffer.length);
      console.log('reading', max, ret, this.buffer);
      resolve(ret);
    });
  }

  toString() {
    return `${this.socket.localAddress}:${this.socket.localPort}-${this.socket.remoteAddress}:${this.socket.remotePort}`;
  }
}

async function main(args) {

  async function sleep(time) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve();
      }, time);
    });
  }

  process.on('unhandledRejection', (reason, p) => {
    console.log('FAIL', reason);
    process.exit(1);
  });

  console.log('args', args);

  let conn = await TcpConn.connect(args[2], args[3]);
  console.log(`connected ${conn}`);
  //await conn.write('01234');
  //await conn.write('56789');
  while(true) {
    let data = await conn.read(3);
    if(!data) {
      break;
    }
    console.log('read', data.toString('utf8'));
    await sleep(5000);
  }
  console.log('done');
}

if (require.main === module) {
    main(process.argv);
}

exports.TcpConn = TcpConn;
