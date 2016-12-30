const idgen = require('./idgen');
const http = require('http');

function Proxy() {
  this.opts = {
    storage: 'requests'
  };
  (function(t) {
    t.server = http.createServer((req, res) => {
      t.req(req, res)
    });
  })(this);
}

Proxy.prototype.start = function(port) {
  this.server.listen(8000);
}

Proxy.prototype.writeRequest = function(req) {

}

Proxy.prototype.req = function(req, res) {
  req.furlId = 'r' + new Date().getTime() + '-' + idgen();
  this.writeRequest(req);
  res.end('xxxxx');
}

exports.Proxy = Proxy
