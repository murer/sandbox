
class HttpServer {

  constructor(params) {
    this.params = params;
  }

  async start() {
    this.port = 5001;
  }

  async stop() {

  }

}

exports.HttpServer = HttpServer;
