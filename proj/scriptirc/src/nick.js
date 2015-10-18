log = require('./log')
util = require('./util')

function init(client) {

    function setNick(nick) {
        client.nickVisual = nick;
        client.nick = util.low(nick);
    }

    function onNickChanged(evt, data) {
        var user = util.parseUser(data.prefix);
        if(user.nick != client.nick) {
            return;
        }
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