log = require('./log')

function init(client, config) {

    function changeNick(n) {
        client.fire('protocol_send', 'NICK ' + config.nick);
    } 

    function onConnected(evt) {
        client.changeNick(config.nick);
        client.fire('protocol_send', 'USER ' + config.user);
    }

    client.on('conns_connected', onConnected);

    Client.fn.changeNick = changeNick;
}

module.exports = init