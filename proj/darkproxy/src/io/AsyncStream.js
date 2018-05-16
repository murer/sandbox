
class AsyncReadable {

  constructor(input) {
    this.input = input;
  }

  read() {
    console.log('read');
  }

}

exports.AsyncReadable = AsyncReadable;
