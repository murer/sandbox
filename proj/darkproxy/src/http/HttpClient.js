
const { URL } = require('url');
/*
class HttpClient {

}
const client = new HttpClient();
*/

class HttpRequest {
  static create(url) {
    let ret = new HttpRequest();
    ret.url = new URL(url);
    return ret;
  }
}

const main = () => {
  let req = HttpRequest.create('http://google.com/');
  console.log(req);
}

if (require.main === module) {
    main(process.argv);
}

//exports.client = client;
