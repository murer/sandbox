import socket
import sys
import struct

def close(o):
    if o:
        try:
            o.close()
        except:
            print >> sys.stderr, "Unexpected error:", sys.exc_info()[0]

def unpadding(obj):
    if type(obj) == tuple:
        return [ unpadding(o) for o in obj ]
    idx = obj.find('\x00')
    if idx >= 0:
        obj = obj[0:idx]
    return obj

def mac2str(bin):
    return ':'.join([ '%02x' % ord(b) for b in bin ])

def ip2str(bin):
    return '.'.join([ str(ord(b)) for b in bin ])

def ip2bin(data):
    return ''.join([ struct.pack('!B', int(d)) for d in data.split('.') ])

class DHCPServer():
    
    def __init__(self):
        self.server_port = 67
        self.server_host = ''

    def bind(self):
        print >> sys.stderr, 'Binding', self.server_host, self.server_port
        self.sck = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        self.sck.bind((self.server_host, self.server_port))

    def close(self):
        print >> sys.stderr, 'Closing'
        close(self.sck)

    def serve(self):
        data, address = self.sck.recvfrom(1024)
        print >> sys.stderr, 'Connection', address, len(data)
        self.conn(client=address, req=data)

    def serve_forever(self):
        print >> sys.stderr, 'Serving forever'
        while True:
            self.serve()

    def parse_request(self, req):
        ret = {}
        ret['op'], ret['htype'], ret['hlen'], ret['hops'] = struct.unpack('!BBBB', req[0:4])
        ret['xid'], ret['secs'], ret['flags'] = struct.unpack('!IHH', req[4:12])
        ret['ciaddr'] = req[12:16]
        ret['ciaddr_str'] = ip2str(ret['ciaddr'])
        ret['yiaddr'] = req[16:20]
        ret['siaddr'] = req[20:24]
        ret['giaddr'] = req[24:28]
        ret['chaddr'] = req[28:34]
        ret['chaddr_str'] = mac2str(ret['chaddr'])
        ret['sname'], ret['file'] = unpadding(struct.unpack('!64s128s', req[40:232]))
        ret['options'] = unpadding(req[232:])
        return ret

    def conn(self, client, req):
        preq = self.parse_request(req)
        print >> sys.stderr, 'Mac', preq['chaddr_str'], preq['ciaddr_str']
        if preq['chaddr_str'] == 'e0:db:55:fe:eb:41':
            self.reply(client, preq)

    def reply(self, client, req):
        print 'Parsing', req['chaddr_str'], req['ciaddr_str'], req
        resp = req.copy()
        resp['op'] = 0x02
        resp['hops'] = 0x00
        resp['secs'] = 0x00
        resp['ciaddr'] = '\x00\x00\x00\x00'
        resp['yiaddr'] = ip2bin('172.16.129.202')
        resp['yiaddr_str'] = ip2str(resp['yiaddr'])
        resp['siaddr'] = '\x00\x00\x00\x00'
        resp['sname'] = ''
        resp['file'] = ''
        resp['options'] = {
            'gateway': ip2bin('172.16.120.1'),
            'dns': [ ip2bin('172.16.120.1'), ip2bin('8.8.8.8') ],
            'bcast': ip2bin('172.16.127.255'),
            'mask': ip2bin('255.255.248.0')
        }
        self.send_resp(client, resp)

    def send_resp(self, client, resp):
        print 'Sending', client, resp['chaddr_str'], resp['yiaddr_str']
        data = struct.pack('!BBBB', resp['op'], resp['htype'], resp['hlen'], resp['hops'])
        data += struct.pack('!IHH', resp['xid'], resp['secs'], resp['flags'])
        data += resp['ciaddr'] + resp['yiaddr'] + resp['siaddr'] + resp['giaddr']
        data += resp['chaddr'] + ('\x00' * 10)
        data += resp['sname'] + ('\x00' * (64-len(resp['sname'])))
        data += resp['file'] + ('\x00' * (128-len(resp['file'])))
        print ' '.join(['%02x' % ord(c) for c in data[:4]])
        print ' '.join(['%02x' % ord(c) for c in data[4:8]])
        print ' '.join(['%02x' % ord(c) for c in data[8:12]])
        print ' '.join(['%02x' % ord(c) for c in data[12:16]])
        print ' '.join(['%02x' % ord(c) for c in data[16:20]])
        print ' '.join(['%02x' % ord(c) for c in data[20:24]])
        print ' '.join(['%02x' % ord(c) for c in data[24:28]])
        print ' '.join(['%02x' % ord(c) for c in data[28:44]])
        print ' '.join(['%02x' % ord(c) for c in data[44:]])
        print len(data)
        #self.sck.sendto(data, ('<broadcast>', 68))


def main():
    try:
        server = DHCPServer()
        server.bind()
        server.serve_forever()
    finally:
        close(server)

if __name__ == '__main__':
    main()
