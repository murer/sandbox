
# Rabin-Miller Primality Tester
p = 11 # generated first prime
q = 13 # generated second prime

# modulus
n = p * q

# totient
t = (p - 1) * (q - 1)

# public exponent is choosen
# must be prime
# 3 <= public_exponent < totient
# can not be multiple of totient
# usually is 65537
e = 7

# private exponent
# Extended Euclidean Algorithm
p = 103
((e * p) % t) == 1

print 'public_key: (%s, %s)' % (e, n)
print 'private_key: (%s, %s)' % (p, n)


public_key = (7, 143)
private_key = (103, 143)

def encrypt(message, key):
    return (message ** key[0]) % key[1]

print encrypt(20, public_key)
print encrypt(136, private_key)
