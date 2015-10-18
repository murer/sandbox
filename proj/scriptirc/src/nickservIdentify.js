log = require('./log')

function init(client) {

    function notice(evt, opts) {
        if(!opts.params[1].match(/^This nickname is registered. Please choose a different nickname, or identify via/)) {
            return;
        }
        if(!client.nickservIdentify) {
            log.debug('No password');
            return;
        }
        if(opts.prefix != 'NickServ!NickServ@services.') {
            log.debug('password requested by someone else than nickserv');
            return;
        }
        client.msg('nickserv', 'identify ' + client.nickservIdentify);
    }

    client.setNickservIdentify = function(nickservIdentify) {
        client.nickservIdentify = nickservIdentify;
    }

    client.on('protocol_command_NOTICE', notice);

}

module.exports = init