log = require('./log')

function init(client) {

    function onUserJoin(evt, channel) {
        client.fire('protocol_send', 'JOIN ' + channel);
    }

    client.join = function() {
        for(var i = 0; i < arguments.length; i++) {
        	client.fire('user_join', arguments[i]);
        }
    };

    client.on('user_join', onUserJoin);
}

module.exports = init
