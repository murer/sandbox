
log = require('./log')
Client = require('./client');
ConnectionAddon = require('./connection');

log.info('Starting')

function main() {
	var client = new Client();
	client.register(new ConnectionAddon());
	client.fire('client_start');

	client.connect('irc.freenode.net', 6667)
}

main();
