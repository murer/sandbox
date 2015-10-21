log = require('./log')
util = require('./util')

function init(client) {
   
    function addHistory(entry) {
        var channel = client.histories.channels[entry.channel];
        if(!channel) {
            channel = {
                last: 0
            };
            client.histories.channels[entry.channel] = channel;
        }
        var id = channel.last++;
        channel[id] = entry;  
    }

    function onMessage(evt, data) {
        var to = data.params[0];
        if(!to.match(/^#/)) {
            return;
        }
        to = util.low(to);
        var msg = data.params[1];
        var from = util.parseUser(data.prefix);
        addHistory({
            type: 'msg',
            date: new Date().getTime(),
            visualFrom: from.nick,
            from: util.low(from.nick),
            channel: to,
            msg: msg
        });
        console.log('xxxxxx', from.nick, to, msg, JSON.stringify(client.histories.channels, null, 4));
    }

    client.histories = {
        channels: {}
    };

    client.on('protocol_command_PRIVMSG', onMessage);

}

module.exports = init