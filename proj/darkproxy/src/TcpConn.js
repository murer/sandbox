const net = require('net');
const util = require('util');
const yld = util.promisify(setImmediate);

class TcpConn {

  static connect(host, port) {
    return new Promise((resolve, reject) => {
      let ret = new TcpConn();
      const errorCallback = (err) => {
        console.log('err', err);
      };
      ret.socket = net.connect(port, host, () => {
        ret.socket.removeListener('error', errorCallback);
        resolve(ret);
      });
      ret.socket.on('error', errorCallback);
    });
  }

  write(data) {
    return new Promise((resolve, reject) => {
      this.socket.write(data, resolve);
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

  await conn.write('0123456789');

  /*
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  await conn.write('0123456789');
  */

  while(true) {
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

  console.log('done', conn.err);
}

if (require.main === module) {
    main(process.argv);
}

exports.TcpConn = TcpConn;
