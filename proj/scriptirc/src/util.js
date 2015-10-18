
function toLowerCase(str) {
    if(!str) {
        return str;
    }
    var ret = '';
    for(var i = 0; i < str.length; i++) {
        var c = str.charAt(i);
        if(c == '[') {
            c = '{';
        } else if(c == ']') {
            c = '}'
        } else if(c == '/') {
            c = '|'
        } else { 
            c = c.toLowerCase();
        }
        ret += c;
    }
    return ret;
}

function parseNick(nick) {
    console.log('parsing', nick);
    var ret = {};
    ret.full = nick;
    if(nick.match(/^[\+\@]/)) {
        ret.flag = nick.charAt(0);
        nick = nick.substring(1);
    }
    ret.visual = nick;
    ret.nick = toLowerCase(nick);
    return ret;
}

function parseNicks() {
    var array = [];
    var ret = {};
    for(var i = 0; i < arguments.length; i++) {
        var subs = arguments[i].split(' ');
        for(var j = 0; j < subs.length; j++) {
            var sub = subs[j];
            var parsed = parseNick(sub);
            ret[parsed.nick] = parsed;
        }
    }
    return ret;
}

function extend(target) {
    for(var i = 1; i < arguments.length; i++) {
        var obj = arguments[i];
        for(var k in obj) {
            target[k] = obj[k];
        }
    }
    return target;
}

function parseUser(full) {
    if(!full) {
        return null;
    }
    var ret = {
        fullVisual: full
    };
    var target = 'nickVisual';
    for(var i = 0; i < full.length; i++) {
        var c = full.charAt(i);
        if(c == '!') {
            target = 'userVisual';
            continue;
        }
        if(c == '@') {
            target = 'hostVisual';
            continue;
        }
        var dest = ret[target] || '';
        ret[target] = dest + c;
    }
    ret.full = toLowerCase(ret.fullVisual);
    ret.host = toLowerCase(ret.hostVisual);
    ret.nick = toLowerCase(ret.nickVisual);
    ret.user = toLowerCase(ret.userVisual);
    return ret;
}

exports.low = toLowerCase;
exports.parseNick = parseNick;
exports.parseNicks = parseNicks;
exports.parseUser = parseUser;
exports.extend = extend;

console.log(JSON.stringify(parseUser('aAa!bbb@ccc'), null, 4));
console.log(JSON.stringify(parseUser('aAa@cCc!bBb'), null, 4));
console.log(JSON.stringify(parseUser('aaa!bbb'), null, 4));
console.log(JSON.stringify(parseUser('aaa@ccc'), null, 4));
console.log(JSON.stringify(parseUser('aaa'), null, 4));
console.log(JSON.stringify(parseUser(''), null, 4));




