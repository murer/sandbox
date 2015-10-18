log = require('./log')

function init(client) {

    var line = '';

    function onConnsData(evt, data) {
        line += data;
        var idx = line.indexOf('\n');
        while(idx >= 0) {
            var ret = line.substring(0, idx);
            ret = ret.replace(/\r/, '');
            line = line.substring(idx + 1);
            idx = line.indexOf('\n');
            client.fire('protocol_data', ret);
        }
    }

    function onProtocolData(evt, data) {
        var array = data.match(/\s*([^\s]+)\s+([^\s]+)\s(.*)$/);
        var msg = { prefix: array[1], command: array[2], params: array[3] };
        client.fire('protocol_message', msg);
    }

    function onSend(evt, data) {
        client.fire('conns_send', data += '\r\n');
    }

    client.on('conns_data', onConnsData);
    client.on('protocol_data', onProtocolData);
    client.on('protocol_send', onSend);
}

module.exports = init