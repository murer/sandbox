
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');
nickservIdentify = require('./nickservIdentify');
nick = require('./nick');
ident = require('./ident');
channel = require('./channel');
ping = require('./ping');
history = require('./history');

function main() {
	log.info('Starting');

	var client = new Client();
	protocol(client);
	ping(client);
	nickservIdentify(client);
	nick(client);
	connsMod(client);
	ident(client);
	channel(client);
	history(client);

	client.setIdent('1 1 1 1');
	client.changeNick('Elemister');
	client.connect({
		host: 'localhost',
		port: 6667
	});

	client.on('protocol_command_004', function() {
		client.changeNick('pyrata');
		client.join('#mycontainer');
	});
	//log.info('Server', server)

	module.exports = client;
}

main();
