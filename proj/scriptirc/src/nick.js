log = require('./log')

function init(client) {

    function setNick(nick) {
        client.nick = nick;
    }

    function onNickChanged(evt, data) {
        setNick(data.params[0]);
        log.info('nick changed to ' + client.nick);
    }

    function onChangeNick(evt, data) {
        if(client.isConnected()) {
            client.fire('protocol_send', 'NICK ' + data);
        } else {
            setNick(data);
        }
    } 

    function onConnected(evt) {
        client.changeNick(client.nick);
    }

    client.changeNick = function(nick) {
        client.fire('user_nick', nick);
    }

    client.on('conns_connected', onConnected);
    client.on('protocol_command_NICK', onNickChanged);
    client.on('user_nick', onChangeNick);

}

module.exports = init