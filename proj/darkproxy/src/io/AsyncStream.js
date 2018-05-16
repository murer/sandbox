
class AsyncReadable {

  constructor(input) {
    this.input = input;
    this.input.on('end', () => {
      this.hasEnded = true;
    });
    this.input.pause();
  }

  read() {
    return new Promise((resolve, reject) => {
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

exports.AsyncReadable = AsyncReadable;
