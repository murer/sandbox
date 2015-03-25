import httplib
import json
from urlparse import urlparse

class Error(Exception):
	"""Exceptions"""

class HttpError(Error):
	"""Exception"""

def req_json(method, url, params = '', headers = {}, expects = [200]):
	parsed = urlparse(url)
	host = parsed.netloc
	uri = parsed.path
	if(parsed.query != ''):
		uri = uri + '?' + parsed.query
	conn = None
	if(parsed.scheme == 'https'):
		conn = httplib.HTTPSConnection(parsed.hostname, parsed.port or 443)
	else: 
		conn = httplib.HTTPConnection(parsed.hostname, parsed.port or 80)
	if parsed.username != None:
		token = parsed.username + ':' + (parsed.password or '')
		token = 'Basic ' + base64.b64encode(token)
		headers['Authorization'] = token
	try:
		conn.request(method, uri, params, headers)
		response = conn.getresponse()
		if response.status not in expects:
			raise Error('Status: %d %s %sri' % (response.status, response.reason, response.read()))
		string = response.read()
		if not string:
			return None
		ret = json.loads(string)
	finally:
		conn.close()
	return ret


def __main():
	obj = req_json('GET', 'http://graph.facebook.com/phmurer')
	print json.dumps(obj)

if __name__ == '__main__':
	__main()
