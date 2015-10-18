
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');
nickservIdentify = require('./nickservIdentify');
nick = require('./nick');
ident = require('./ident');

function main() {
	log.info('Starting');

	var client = new Client();
	protocol(client);
	nickservIdentify(client);
	nick(client);
	connsMod(client);
	ident(client);

	client.setIdent('1 1 1 1');
	client.changeNick('pyrata');
	client.setNickservIdentify('1q2w3e4r');
	client.connect({
		host: 'localhost',
		port: 6667
	});
	//log.info('Server', server)
}

main();
