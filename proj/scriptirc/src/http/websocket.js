var http = require('http')
var crypto = require('crypto')

function onRequest(req, resp) {
    console.log('q', req.url);
    req.on('data', function() {
        res.writeHead(200, {'Content-Type': 'text/plain'});
        res.end('okay');
    });
}

server = http.createServer(onRequest);
server.listen(5000);

function generateMetaData(fin, opcode, masked, payload) {
    var len, meta, start, mask, i
    len = payload.length
    meta = new Buffer(2 + (len < 126 ? 0 : (len < 65536 ? 2 : 8)) + (masked ? 4 : 0))
    meta[0] = (fin ? 128 : 0) + opcode
    meta[1] = masked ? 128 : 0
    start = 2
    if (len < 126) {
        meta[1] += len
    } else if (len < 65536) {
        meta[1] += 126
        meta.writeUInt16BE(len, 2)
        start += 2
    } else {
        meta[1] += 127
        meta.writeUInt32BE(Math.floor(len / Math.pow(2, 32)), 2)
        meta.writeUInt32BE(len % Math.pow(2, 32), 6)
        start += 8
    }
    if (masked) {
        mask = new Buffer(4)
        for (i = 0; i < 4; i++) {
            meta[start + i] = mask[i] = Math.floor(Math.random() * 256)
        }
        for (i = 0; i < payload.length; i++) {
            payload[i] ^= mask[i % 4]
        }
        start += 4
    }
    return meta
}

server.on('upgrade', function(req, socket, head) {
    console.log('upgrade');

    var key = req.headers['sec-websocket-key'];
    var sha1 = crypto.createHash('sha1'); 
    sha1.end(key + '258EAFA5-E914-47DA-95CA-C5AB0DC85B11');
    key = sha1.read().toString('base64');
    console.log('key', key); 

    socket.write('HTTP/1.1 101 Web Socket Protocol Handshake\r\n' +
               'Upgrade: WebSocket\r\n' +
               'Connection: Upgrade\r\n' +
               'Sec-WebSocket-Accept: ' + key + '\r\n' +
               'Sec-WebSocket-Version: 13\r\n' +
               '\r\n');

    socket.on('data', function(data){
        console.log('data', data);
        socket.write(data);
        var payload = new Buffer('test - murer');
        var meta = generateMetaData(true, 1, true, payload);
        var ret = Buffer.concat([meta, payload], meta.length + payload.length);
        console.log('ret', ret);
        socket.write(ret);
    });

});
