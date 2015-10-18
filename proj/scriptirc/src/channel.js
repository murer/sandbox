log = require('./log')

function init(client) {

    function onUserJoin(evt, channel) {
        client.fire('protocol_send', 'JOIN ' + channel);
    }

    function onJoin(evt, data) {
    	var channel = data.params[0];
    	client.channels[channel] = {};
    	log.debug('Join', channel);
    }

    client.join = function() {
        for(var i = 0; i < arguments.length; i++) {
        	client.fire('user_join', arguments[i]);
        }
    };

    client.channels = {};

    client.on('user_join', onUserJoin);
    client.on('protocol_command_JOIN', onJoin);
}

module.exports = init
