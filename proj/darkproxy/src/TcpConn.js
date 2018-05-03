const net = require('net');
const util = require('util');
const yld = util.promisify(setImmediate);

class TcpConn {
  static connect(host, port) {
    return new Promise((resolve, reject) => {
      let ret = new TcpConn();
      ret.buffer = Buffer.from([]);
      ret.state = 'CREATED';
      ret.socket = net.connect(port, host, () => {
        ret.state = 'CONNECTED';
        resolve(ret);
      });
      console.log('readableHighWaterMark', ret.socket.readableHighWaterMark)

      ret.socket.on('error', (err) => {
        if(ret.state === 'CREATED') {
          return reject(err);
        }
        console.log('error', err);
      });

      /*
      ret.socket.on('end', () => {
        console.log('connection end');
      });
      */

      ret.socket.on('close', (hasError) => {
        console.log('connection close', hasError);
        ret.state = 'CLOSED';
      });

    });
  }

  async write(data) {
    return new Promise(async (resolve, reject) => {
      console.log('write', data);
      this.socket.write(data);
      await yld();
      resolve();
    });
  }

  async read(max) {
    return new Promise(async (resolve, reject) => {
      await yld();

      if(max === 0) {
        return resolve(Buffer.from([]));
      }
      let data = this.socket.read();
      console.log('internal read', max, data, this.state);
      if(!data && this.state === 'CLOSED') {
        console.log('reading closed socket');
        return null;
      }
      if(!data) {
        this.socket.once('readable', async () => {
          console.log('readable');
          resolve(await this.read(max));
        });
        return;
      }
      console.log('internal data', data.length, max);
      if(data.length < max) {
        return resolve(data);
      }
      let ret = data.slice(0, max);
      let remaining = data.slice(max, data.length);
      console.log('unshift', remaining.length);
      this.socket.unshift(remaining);
      resolve(ret);
      /*
      console.log('check resume', max, this.buffer.length, this.socket.isPaused());
      if(max > this.buffer.length) {
        console.log('resume');
        this.socket.resume();
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
      console.log('reading', max, ret.length, this.buffer.length);
      resolve(ret);
      */
    });
  }

  async end(data) {
    return new Promise(async (resolve, reject) => {
      console.log('write end', data, this.socket.allowHalfOpen);
      this.socket.end(data);
      await yld();
      resolve();
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

  let conn = await TcpConn.connect(args[2], args[3]);
  console.log(`connected ${conn}`);

  await conn.write('01234');
  await conn.write('56789');

  while(true) {
    conn.write('123')
    let data = await conn.read(3);
    if(!data) {
      break;
    }
    console.log('read', data.length, conn.socket.bufferLength);
    await sleep(1000);
  }


  /*
  console.log('data1', await conn.read(3));
  await conn.end();
  console.log('data2', await conn.read(3));
  console.log('data3', await conn.read(10));
  console.log('data4', await conn.read(10));
  console.log('data5', await conn.read(10));
  console.log('data6', await conn.read(10));
  */

  console.log('done');
}

if (require.main === module) {
    main(process.argv);
}

exports.TcpConn = TcpConn;
