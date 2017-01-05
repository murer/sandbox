
function _byDate(a, b) {
    var aDate = a.req.created;
    var bDate = b.req.created;
    console.log('x', aDate, bDate, a.id, b.id)
    var ret = aDate - bDate;
    if(ret == null) {
        ret = a.id.localeCompare(b.id);
    }
    return ret;
}

function MessageHolder() {
    this._map = {};
}

MessageHolder.prototype.add = function(msg) {
    this._map[msg.data.id] = msg;
}

MessageHolder.prototype.get = function(id) {
    return this._map[id] || null;
}

MessageHolder.prototype.remove = function(id) {
    console.log('delete', id, this._map[id]);
    delete(this._map[id]);
}

MessageHolder.prototype.toList = function() {
    var ret = [];
    for(var key in this._map) {
        ret.push(this._map[key].data);
    }
    ret.sort(_byDate);
    return ret;
}

module.exports = MessageHolder;
