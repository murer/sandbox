import fcntl
import socket
import struct
import sys
import random
import json
import argparse

def close(o):
    if o:
        try:
            o.close()
        except:
            print >> sys.stderr, "Unexpected error:", sys.exc_info()[0]

class DHCPRequest():

    def prepare(self, ifname):
        try:
            print >> sys.stderr, 'Preparing', ifname
            self.ifname = ifname
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            info = fcntl.ioctl(s.fileno(), 0x8927,  struct.pack('256s', self.ifname[:15]))
            self.macb = info[18:24]
            self.mac = ':'.join(['%02x' % ord(char) for char in self.macb])
            print >> sys.stderr, 'Mac', self.mac
        finally:
            close(s)
        self.transactionID = b''
        for i in range(4):
            t = random.randint(0, 255)
            self.transactionID += struct.pack('!B', t)

    def bind(self):
        print >> sys.stderr, 'Binding'
        self.sck = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.sck.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        self.sck.bind(('', 68))

    def close(self):
        print >> sys.stderr, 'Closing'
        close(self.sck)

    def build_packet(self):
        packet = b''
        packet += b'\x01'   #Message type: Boot Request (1)
        packet += b'\x01'   #Hardware type: Ethernet
        packet += b'\x06'   #Hardware address length: 6
        packet += b'\x00'   #Hops: 0 
        packet += self.transactionID       #Transaction ID
        packet += b'\x00\x00'    #Seconds elapsed: 0
        packet += b'\x80\x00'   #Bootp flags: 0x8000 (Broadcast) + reserved flags
        packet += b'\x00\x00\x00\x00'   #Client IP address: 0.0.0.0
        packet += b'\x00\x00\x00\x00'   #Your (client) IP address: 0.0.0.0
        packet += b'\x00\x00\x00\x00'   #Next server IP address: 0.0.0.0
        packet += b'\x00\x00\x00\x00'   #Relay agent IP address: 0.0.0.0
        #packet += b'\x00\x26\x9e\x04\x1e\x9b'   #Client MAC address: 00:26:9e:04:1e:9b
        packet += self.macb
        packet += b'\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00'   #Client hardware address padding: 00000000000000000000
        packet += b'\x00' * 67  #Server host name not given
        packet += b'\x00' * 125 #Boot file name not given
        packet += b'\x63\x82\x53\x63'   #Magic cookie: DHCP
        packet += b'\x35\x01\x01'   #Option: (t=53,l=1) DHCP Message Type = DHCP Discover
        #packet += b'\x3d\x06\x00\x26\x9e\x04\x1e\x9b'   #Option: (t=61,l=6) Client identifier
        packet += b'\x3d\x06' + self.macb
        packet += b'\x37\x03\x03\x01\x06'   #Option: (t=55,l=3) Parameter Request List
        packet += b'\xff'   #End Option
        self.request_data = packet

    def request(self):
        self.build_packet()
        self.sck.sendto(self.request_data, ('<broadcast>', 67))

    def parse_response(self):
        data = self.response_data
        self.response = {
            'offer_ip': '.'.join([str(ord(char)) for char in data[16:20]]),
            'next_server_ip': '.'.join([str(ord(char)) for char in data[20:24]]),
            'server_ident': '.'.join([str(ord(char)) for char in data[245:249]]),
            'lease_time': str(struct.unpack('!L', data[251:255])[0]),
            'subnetMask': '.'.join([str(ord(char)) for char in data[257:261]]),
            'gateway': '.'.join([str(ord(char)) for char in data[263:267]]),
            'dns': []
        }
        dns_length = ord(data[268])/4
        for i in range(0, dns_length):
            first = i * 4
            self.response['dns'].append('.'.join([str(ord(char)) for char in data[269+first:269+first+4]]))

    def receive(self):
        self.sck.settimeout(3)
        while True:
            data = self.sck.recv(1024)
            if data[4:8] == self.transactionID:
                self.response_data = data;
                self.parse_response()
                return

def main():
    parser = argparse.ArgumentParser(description='DHCP Request')
    parser.add_argument('-i', '--interface', required=True, help='interface')
    args = parser.parse_args()
    ifname = args.interface
    req = DHCPRequest()
    req.prepare(ifname)
    req.bind()
    try:
        req.request()
        req.receive()
        print json.dumps(req.response, indent=True)
    finally:
        close(req)

if __name__ == '__main__':
    main()
