log = require('./log')

function init(client, config) {

    function changeNick(evt, data) {
        client.fire('protocol_send', 'NICK ' + data);
    } 

    function onConnected(evt) {
        client.fire('user_nick', config.nick);
        client.nick = config.nick;
        client.fire('protocol_send', 'USER ' + config.user);
    }

    function ready(evt) {
        client.fire('nick_changed', config);
    }

    client.on('conns_connected', onConnected);
    client.on('user_nick', changeNick);
}

module.exports = init