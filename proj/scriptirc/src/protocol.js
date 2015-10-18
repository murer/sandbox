log = require('./log')

function parseLine(line) {
    if(!line) {
        return '';
    }
    var ret = [];
    var pos = 0;
    var end = 0;
    while(end >= 0) {
        if (line[pos] == ':') {
            ret.push(line.substring(pos + 1));
            return ret;
        }
        end = line.indexOf(' ', pos);
        if(end >= 0) {
            ret.push(line.substring(pos, end));
        }
        pos = end + 1;
    }
    /** wrong
    while((end = line.indexOf(' ', pos)) >= 0) {
        ret.push(line.substring(pos, end));
        pos = end + 1;
        if (line[pos] == ':') {
            ret.push(line.substring(pos + 1));
            return ret;
        }
    }
    */
    ret.push(line.substring(pos));
    return ret;
} 

function init(client) {

    var line = '';

    function onConnsData(evt, data) {
        line += data;
        var idx = line.indexOf('\n');
        while(idx >= 0) {
            var ret = line.substring(0, idx);
            ret = ret.trim();
            line = line.substring(idx + 1);
            idx = line.indexOf('\n');
            client.fire('protocol_data', ret);
        }
    }

    function onProtocolData(evt, data) {
        var array = data.match(/^:\s*([^\s]+)\s+([^\s]+)\s(.*)$/);
        var msg = null;
        if(array) {
            msg = { prefix: array[1], command: array[2], params: array[3] };
        } else {
            array = data.match(/\s*([^\s]+)\s+([^\s]+)\s(.*)$/);
            msg = { command: array[2], params: array[3] };
        }
        msg.params = parseLine(msg.params);
        msg.command = msg.command.toUpperCase();
        client.fire('protocol_message', msg);
    }

    function onProtocolMessage(evt, data) {
        client.fire('protocol_command_' + data.command, data);
    }

    function onSend(evt, data) {
        client.fire('conns_send', data += '\r\n');
    }

    client.on('conns_data', onConnsData);
    client.on('protocol_data', onProtocolData);
    client.on('protocol_send', onSend);
    client.on('protocol_message', onProtocolMessage);
}

module.exports = init