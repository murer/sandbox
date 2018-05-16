const { Readable, Writable } = require('stream');

class MemoryReadable extends Readable {

  constructor(...data) {
    super();
    this.data = data || [];
  }

  _read() {
    if(!this.data.length) {
      return this.push(null);
    }
    let ret = this.data.shift();
    if(ret === null) {
      return process.nextTick(() => this.emit('error', new Error('failed')));
      //throw new Error('failed');
    }
    this.push(ret);
  }

}

class MemoryWritable extends Writable {

  constructor() {
    super();
    this.chunks = [];
  }

  _write(chunk, encoding, callback) {
    setTimeout(() => {
      this.chunks.push(chunk);
      setTimeout(callback, 0);
    }, 0);
  }

}

exports.MemoryReadable = MemoryReadable;
exports.MemoryWritable = MemoryWritable;
