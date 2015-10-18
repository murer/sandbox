
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');
nick = require('./nick');

function main() {
	log.info('Starting');

	var client = new Client();
	conns = connsMod(client);
	protocol(client);
	nick(client, {
		nick: 'pyrata',
		user: '1 1 1 1'
	});
	conns.connect({ 
		host: 'localhost', 
		port: 6667
	});
	//log.info('Server', server)
}

main();
