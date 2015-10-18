log = require('./log')

function init(client) {

    function connect(host, port) {
        var sck = net.connect({host: host, port: port}, function() {
        	client.fire('conns_connected');
        });
        sck.setEncoding('UTF-8');
        sck.on('data', function(data) {
        	client.fire('conns_data', data)
        })
        sck.on('close', function() {
        	client.fire('conns_closed');	
        });
    }

    return {
        connect: connect
    }

}

module.exports = init