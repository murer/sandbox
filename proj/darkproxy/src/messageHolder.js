

function MessageHolder() {
    this._map = {};
}

MessageHolder.prototype.add = function(msg) {
    this._map[msg.data.id] = msg;
}

module.exports = MessageHolder;
