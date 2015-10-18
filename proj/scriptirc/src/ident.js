log = require('./log')

function init(client) {

    function onConnected(evt) {
        client.fire('protocol_send', 'USER ' + client.ident);
    }

    client.setIdent = function(ident) {
        client.ident = ident;
    };

    client.on('conns_connected', onConnected);
}

module.exports = init