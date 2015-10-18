net = require('net')
log = require('./log')
basics = require('./basics')

function on(evt, func) {
    log.debug('on', evt, func);
    var addons = this.addons[evt] || [];
    addons.push(func);
    this.addons[evt] = addons;
}

function fire(evt, args) {
    if(evt != 'protocol_data' && evt != 'protocol_message' && evt != 'conns_data' 
        && evt != 'conns_send') {
        log.trace('firing', evt, args);
    }
    var addons = this.addons[evt] || [];
    for (var i = 0; i < addons.length; i++) {
        var result = addons[i](evt, args);
        if(result === false) {
            log.debug('fire halted', evt, addons[i]);
            break;
        }
    }
}

function Client() {
    this.addons = {};
}
Client.fn = Client.prototype;
Client.fn.fire = fire;
Client.fn.on = on;

basics(Client);

module.exports = Client;
