log = require('./log')
util = require('./util');

function init(client) {

    function onUserJoin(evt, channel) {
        client.fire('protocol_send', 'JOIN ' + channel);
    }

    function destroyChannel(name) {
        delete(client.channels[name]);
    }

    function createChannel(data) {
        var channelVisual = data.params[0];
        var channel = util.low(channelVisual);
        client.channels[channel] = {
            visual: channelVisual,
            nicks: {}
        };
        log.debug('Join', channel);
    }

    function addChannelNick(channelName, nicks) {
        var channel = client.channels[channelName];
        var parsed = util.parseNicks(nicks);
        util.extend(channel.nicks, parsed);
    }

    function onJoin(evt, data) {
        var user = util.parseUser(data.prefix);
        if(user.nick == client.nick) {
            createChannel(data);
            return;
        }
        addChannelNick(data.params[0], user.nickVisual);
    }

    function onNickList(evt, data) {
        var channelName = util.low(data.params[2]);
        var nicks = data.params[3];
        addChannelNick(channelName, nicks);
    }

    function onPart(evt, data) {
        var user = util.parseUser(data.prefix);
        var channelName = data.params[0];
        if(user.nick == client.nick) {
            destroyChannel(channelName);
            return;
        }
        var channel = client.channels[channelName];
        delete(channel.nicks[user.nick]);
    }

    client.join = function() {
        for(var i = 0; i < arguments.length; i++) {
            client.fire('user_join', arguments[i]);
        }
    };

    client.channels = {};

    client.on('user_join', onUserJoin);
    client.on('protocol_command_JOIN', onJoin);
    client.on('protocol_command_PART', onPart);
    client.on('protocol_command_353', onNickList);
}

module.exports = init
