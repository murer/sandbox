
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');
nickservIdentify = require('./nickservIdentify');
nick = require('./nick');

function main() {
	log.info('Starting');

	var client = new Client();
	protocol(client);
	nickservIdentify(client);
	nick(client);
	connsMod(client);

	client.nick = 'pyrata';
	client.user = '1 1 1 1';
	client.nickservPassword = '1q2w3e4r';

	client.fire('user_connect', { 
		host: 'localhost', 
		port: 6667
	});
	//log.info('Server', server)
}

main();
