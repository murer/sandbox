from socket import *
import struct
SO_ORIGINAL_DST = 80
s = socket(AF_INET, SOCK_STREAM)
s.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
s.bind(('0.0.0.0', 5000))
s.listen(1)
conn, addr = s.accept()

dst = conn.getsockopt(SOL_IP, SO_ORIGINAL_DST, 16)
srv_port, srv_ip = struct.unpack("!2xH4s8x", dst)
print("original %s:%d" % (inet_ntoa(srv_ip), srv_port))

