log = require('./log')

function init(client) {

    function onConnect(evt, opts) {
        client.sck = net.connect({ host: opts.host, port: opts.port }, function() {
        	client.fire('conns_connected');
        });
        client.sck.setEncoding('UTF-8');
        client.sck.on('data', function(data) {
        	client.fire('conns_data', data);
        })
        client.sck.on('close', function() {
            client.sck = null;
        	client.fire('conns_closed');
        });
    }

    function onSend(evt, data) {
    	client.sck.write(data);
    }

    client.isConnected = function() {
        return !!(client.sck);
    }

    client.connect = function(opts) {
        client.fire('user_connect', opts);
    }

    client.on('conns_send', onSend);
    client.on('user_connect', onConnect);

}

module.exports = init