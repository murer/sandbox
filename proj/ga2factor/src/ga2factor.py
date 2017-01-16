import hmac
import base64
import struct
import hashlib
import time

def get_token(secret):
    now = int(time.time()) # epoch in seconds
    intervals_no = now // 30 # number of 30 seconds interval
    msg = struct.pack('>Q', intervals_no) # msg is binary
    key = base64.b32decode(secret, True) # decode key (base32)
    h = hmac.new(key, msg, hashlib.sha1).digest() # hmac/sha1 using key/msg (20 bytes)
    offset = ord(h[19]) & 0x0F # make first 4 bits zero of last byte -> [0, 16]
    # get 4 bytes from hash starting on offset
    h = (struct.unpack('>I', h[offset:offset+4])[0] & 0x7fffffff)
    # the result is this number mod 10^6
    h = h % 1000000
    return h

def __main():
    print get_token('mysecret')

if __name__ == '__main__':
	__main()
