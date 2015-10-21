log = require('./log')
util = require('./util')

var INTERVAL = 60000;

function init(client) {

    function onPing(evt, data) {
        var target = data.params[0];
        client.fire('protocol_send', 'PONG ' + target);
    }

    function onConnsData(evt, data) {
        if(!data) {
            return;
        }
        if(!client.ping.intervalId) {
            client.ping.intervalId = setInterval(checkTimeout, INTERVAL);
        }
        client.ping.lastDateSent = new Date();
    }

    function onConnsClosed() {
        if(client.ping.intervalId) {
            clearInterval(client.ping.intervalId);
        }
        client.ping = {};
    }

    function checkTimeout() {
        var last = client.ping.lastDateSent.getTime();
        var now = new Date().getTime();
        if( (last + INTERVAL) < now) {
            log.debug('Sending ping because ee have not received anything from server for more than ' + INTERVAL + ' millis');
            client.fire('protocol_send', 'PING ' + client.serverIdent);
        }
    }

    client.ping = {};

    client.on('conns_data', onConnsData);
    client.on('conns_closed', onConnsClosed);
    client.on('protocol_command_PING', onPing);

}

module.exports = init