log = require('./log')

function init(client) {

	var line = '';

	function onData(evt, data) {
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

	client.on('conns_data', onData);
}

module.exports = init