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
    this.push(this.data.shift());
  }

}

exports.MemoryReadable = MemoryReadable;
