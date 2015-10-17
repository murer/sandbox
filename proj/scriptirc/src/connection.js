log = require('./log')

function connect(host, port) {

}

function init(client) {
	this.client = client;
	client.command('connect', connect);
}

function ConnectionAddon() {}
ConnectionAddon.prototype.init = init;

module.exports = ConnectionAddon

