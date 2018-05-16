const { Readable } = require('stream');

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
    }
    this.push(ret);
  }

}

exports.MemoryReadable = MemoryReadable;
