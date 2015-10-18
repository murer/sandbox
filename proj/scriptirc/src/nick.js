log = require('./log')

function init(client) {

    function changeNick(evt, data) {
        client.fire('protocol_send', 'NICK ' + data);
    } 

    function onConnected(evt) {
        client.fire('user_nick', client.nick);
        client.fire('protocol_send', 'USER ' + client.user);
    }

    client.on('conns_connected', onConnected);
    client.on('user_nick', changeNick);
}

module.exports = init