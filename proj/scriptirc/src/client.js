log = require('./log')

function register(addon) {
	log.debug('registering addon', addon);
	this.addons.push(addon);
	addon.init(this);
}

function fire(evt, args){
	log.debug('firing', evt, args)
	var addons = this.addons[evt] || [];
	for (var i = 0; i < addons.length; i++) {
		var result = addons[i](evt, args);
		if(result === false) {
			break;
		}
	}
}

function command(name, cmd) {
	log.debug('Registering command', name, cmd)
	this[name] = cmd;
}

function Client() {
	this.addons = [];
}
Client.prototype.fire = fire;
Client.prototype.register = register;
Client.prototype.command = command;

module.exports = Client;
