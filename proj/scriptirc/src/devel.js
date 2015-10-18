
log = require('./log')
Client = require('./client');
connsMod = require('./conns');
protocol = require('./protocol');
nickservIdentify = require('./nickservIdentify');
nick = require('./nick');
ident = require('./ident');
channel = require('./channel');

function main() {
	log.info('Starting');

	var client = new Client();
	protocol(client);
	nickservIdentify(client);
	nick(client);
	connsMod(client);
	ident(client);
	channel(client);

	client.setIdent('1 1 1 1');
	client.changeNick('elemister');
	client.setNickservIdentify('1q2w3e4r');
	client.connect({
		host: 'localhost',
		port: 6667
	});

	client.on('nick_identified', function() {
		client.changeNick('pyrata');
		client.join('#mycontainer');
	});
	//log.info('Server', server)
}

main();
