log = require('./log')

function init(client) {

	function notice(evt, opts) {
		if(!client.nickservPassword) {
			return;
		}
		if(opts.prefix != 'NickServ!NickServ@services.') {
			return;
		}
		if(opts.params[0] != client.nick) {
			return;
		}
		if(opts.params[1] && opts.params[1].match(/^This nickname is registered. Please choose a different nickname, or identify via/)) {
			client.msg('nickserv', 'identify ' + client.nickservPassword);
		}
	}

	client.on('protocol_command_NOTICE', notice);

}

module.exports = init