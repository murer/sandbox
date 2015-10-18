
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');

function main() {
	log.info('Starting');

	var client = new Client();
	conns = connsMod(client);
	protocol(client);
	conns.connect('irc.freenode.net', 6667);
	//log.info('Server', server)
}

main();
