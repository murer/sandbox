log = require('./log')

function init(client, config) {

    function changeNick(evt, n) {
        client.fire('protocol_send', 'NICK ' + config.nick);
    } 

    function onConnected(evt) {
    	client.fire('user_nick', config.nick);
        client.fire('protocol_send', 'USER ' + config.user);
    }

    client.on('conns_connected', onConnected);
    client.on('user_nick', changeNick);
}

module.exports = init