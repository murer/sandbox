import socket
import traceback

host = ''                               # Bind to all interfaces
port = 67

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
s.bind((host, port))

while 1:
    try:
        data, address = s.recvfrom(8192)
        print "Got data from", address
        print repr(data)
        print "--"
        # Acknowledge it.
        #s.sendto("I am here", address)
    except (KeyboardInterrupt, SystemExit):
        raise
    except:
        traceback.print_exc()
