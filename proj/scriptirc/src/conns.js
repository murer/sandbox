log = require('./log')

function init(client) {

	var sck = null;

    function onConnect(evt, opts) {
        sck = net.connect({ host: opts.host, port: opts.port }, function() {
        	client.fire('conns_connected');
        });
        sck.setEncoding('UTF-8');
        sck.on('data', function(data) {
        	client.fire('conns_data', data);
        })
        sck.on('close', function() {
        	client.fire('conns_closed');	
        });
    }

    function onSend(evt, data) {
    	sck.write(data);
    }

    client.on('conns_send', onSend);
    client.on('connection_connect', onConnect);

}

module.exports = init