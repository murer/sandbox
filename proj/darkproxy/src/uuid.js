var rnd = require('crypto').randomBytes;

function UUID() {
    var buffer = rnd(16);
    buffer[6] = (buffer[6] & 0x0F) | 0x40;
    buffer[8] = (buffer[8] & 0x3F) | 0x80;
    this.buffer = buffer;
}

UUID.prototype.format = function() {
    return this.buffer.slice(0, 4).toString('hex') +
      '-' + this.buffer.slice(4, 6).toString('hex') +
      '-' + this.buffer.slice(6, 8).toString('hex') +
      '-' + this.buffer.slice(8, 10).toString('hex') +
      '-' + this.buffer.slice(10, 16).toString('hex');
}

module.exports = UUID;
