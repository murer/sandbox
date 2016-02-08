net = require('net');

exports.server = function(port) {
  var server = net.createServer(function(socket) {
    socket.write('Connected\n');
    var name = socket.remoteAddress + ":" + socket.remotePort

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
    });
  });
  server.listen(port, function() {
    address = server.address();
    console.log('Reply server started on %j', address, arguments);
  });
}

exports.server(5000);
