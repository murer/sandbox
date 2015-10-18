log = require('./log')

function init(client) {

    function onNickChanged(evt, data) {
        client.nick = data.params[0];
        log.info('nick changed to ' + client.nick);
    }

    function onChangeNick(evt, data) {
        if(client.isConnected()) {
            client.fire('protocol_send', 'NICK ' + data);
        } else {
            client.nick = data;
        }
    } 

    function onConnected(evt) {
        client.fire('user_nick', client.nick);
    }

    client.changeNick = function(nick) {
        client.fire('user_nick', nick);
    }

    client.on('conns_connected', onConnected);
    client.on('protocol_command_NICK', onNickChanged);
    client.on('user_nick', onChangeNick);

}

module.exports = init