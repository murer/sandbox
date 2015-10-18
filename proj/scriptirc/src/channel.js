log = require('./log')
util = require('./util');

function init(client) {

    function handleLeave(channelName, nick) {
        if(nick == client.nick) {
            delete(client.channels[channelName]);
            return;
        }
        var channel = client.channels[channelName];
        delete(channel.nicks[nick]);
    }

    function onUserJoin(evt, channel) {
        client.fire('protocol_send', 'JOIN ' + channel);
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
        handleLeave(channelName, user.nick);
    }

    function onKick(evt, data) {
        handleLeave(data.params[0], util.low(data.params[1]));
    }

    function onQuit(evt, data) {
        var user = util.parseUser(data.prefix);
        for(var channelName in client.channels) {
            console.log('handle quit', user.nick, channelName);
            handleLeave(channelName, user.nick);
        }
    }

    function onNick(evt, data) {
        var fromUser = util.parseUser(data.prefix);
        var from = fromUser.nick;
        var toVisual = data.params[0];
        var to = util.low(toVisual);
        for(var channelName in client.channels) {
            var channel = client.channels[channelName];
            if(channel.nicks[from]) {
                channel.nicks[to] = channel.nicks[from];
                delete(channel.nicks[from]);
                channel.nicks[to].full = toVisual;
                channel.nicks[to].visual = toVisual;
                channel.nicks[to].nick = to;                
            }
        }
        console.log('AAA', client);
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
    client.on('protocol_command_KICK', onKick);
    client.on('protocol_command_QUIT', onQuit);
    client.on('protocol_command_NICK', onNick);
}

module.exports = init
