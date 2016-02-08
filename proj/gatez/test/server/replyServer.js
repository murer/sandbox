net = require('net');

exports.server = function(port) {
  var server = net.createServer(function(socket) {
    socket.write('Hello\n');

    socket.on('data', function(data) {
      var msg = socket.remoteAddress + ":" + socket.remotePort + "> '" + data.toString('utf-8') + "'";
      console.log(msg);
      socket.write(msg);
      if(data.toString('utf-8').match(/^exit/)) {
        socket.end('Done')
      }
    });
    socket.on('end', function() {
      console.log('disconnected from server', arguments);
    });
  });
  server.listen(port, function() {
    address = server.address();
    console.log('opened server on %j', address, arguments);
  });

}

exports.server(5000);
