log = require('./log')

function init(client) {

    function checkNickserv(opts) {
        return (opts.prefix == 'NickServ!NickServ@services.');
    }

    function identify(opts) {
        if(!checkNickserv(opts)) {
            log.debug('password requested message was not sent by nickserv');
            return;
        }
        if(!client.nickservIdentify) {
            log.debug('No password');
            return;
        }
        client.msg('nickserv', 'identify ' + client.nickservIdentify);
    }

    function identified(opts) {
        if(!checkNickserv(opts)) {
            log.debug('identify success message was not sent by nickserv');
            return;
        }
        client.fire('nick_identified')
    }

    function notice(evt, opts) {
        if(opts.params[1].match(/^This nickname is registered. Please choose a different nickname, or identify via/)) {
            identify(opts);
            return;
        }
        if(opts.params[1].match(/^You are now identified for/)) {
            identified(opts);
            return;
        }
    }

    client.setNickservIdentify = function(nickservIdentify) {
        client.nickservIdentify = nickservIdentify;
    }

    client.on('protocol_command_NOTICE', notice);

}

module.exports = init