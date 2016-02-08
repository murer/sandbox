net = require('net');

function Server(port) {
    var clients = {};
    var server = net.createServer(function(socket) {
      var name = socket.remoteAddress + ":" + socket.remotePort
      clients[name] = socket;
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
        delete(clients[name])
      });
    });
    server.listen(port, function() {
      address = server.address();
      console.log('Reply server started on %j', address, arguments);
    });

    this.clients = clients;
    this.server = server;
}

Server.prototype.shutdown = function() {
  console.log('Shutting down')
  this.server.close(function() {
    console.log('Server shutdown')
  });
  for(var name in this.clients) {
    this.clients[name].end('Shutting down\n');
    delete(this.clients[name]);
  };
}

exports.server = function(port) {
  return new Server(port);
}
