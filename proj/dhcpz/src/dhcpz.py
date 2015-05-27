import socket
import sys
import struct

def close(o):
    if o:
        try:
            o.close()
        except:
            print >> sys.stderr, "Unexpected error:", sys.exc_info()[0]

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
        print ret
        
    def conn(self, client, req):
        parsed_req = self.parse_request(req)
        print parsed_req

def main():
    try:
        server = DHCPServer()
        server.bind()
        server.serve_forever()
    finally:
        close(server)

if __name__ == '__main__':
    main()
