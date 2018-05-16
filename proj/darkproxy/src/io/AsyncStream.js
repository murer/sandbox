
class AsyncStream {

  constructor(stream) {
    this.input = stream;
    this.input.on('end', () => {
      this.hasEnded = true;
    });
    if(this.input.pause) {
      this.input.pause();
    }

    this.output = stream;
    this.output.on('error', err => this.err = err);
  }

  write(chunk, encoding) {
    return new Promise((resolve, reject) => {
      this.output.write(chunk, encoding, (err) => {
        if(err) {
          return reject(err);
        }
        resolve();
      });
    });
  }

  async end(chunk, encoding) {
    await this.write(chunk, encoding);
  }

  read() {
    return new Promise((resolve, reject) => {
      if(this.err) {
        return reject(this.err);
      }
      if(this.hasEnded) {
        return resolve(null);
      }
      this.input.once('data', (data) => {
        this.input.pause();
        resolve(data);
      });
      this.input.resume();
    });
  }

}

exports.AsyncStream = AsyncStream;
