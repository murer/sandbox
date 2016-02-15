net = require('net');

function Server() {
}

Server.prototype.shutdown = function(onFinished) {
  console.log('Shutting down')
  this.server.close(function() {
    console.log('Server shutdown')
  });
  for(var name in this.clients) {
    this.clients[name].end('Shutting down\n');
    delete(this.clients[name]);
  };
  onFinished(this);
}

exports.listen = function(port, onStarted) {

  var server = new Server(port);

  server.clients = {};
  server.server = net.createServer(function(socket) {
    var name = socket.remoteAddress + ":" + socket.remotePort
    server.clients[name] = socket;
    socket.write('Connected\n');

    socket.on('data', function(data) {
      var msg = data.toString('utf-8');
      var reply = 'Reply: ' + data;
      socket.write(reply);
      if(msg.match(/^exit/)) {
        socket.end();
      }
    });
    socket.on('end', function() {
      console.log('Client Disconnected: ' + name);
      delete(server.clients[name])
    });
  }).listen(port, function() {
    onStarted(server);
  });


  return new Server(port);
}
