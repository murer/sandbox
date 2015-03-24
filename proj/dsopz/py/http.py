import settings
import httplib
import json as jsonparser
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
		conn = httplib.HTTPSConnection(host)
	else: 
		conn = httplib.HTTPConnection(host)
	try:
		conn.request(method, uri, params, headers)
		response = conn.getresponse()
		if response.status not in expects:
			raise HttpError('Status: %d %s %sri' % (response.status, response.reason, response.read()))
		string = response.read()
		if(string == ''):
			return None
		ret = jsonparser.loads(string)
	finally:
		conn.close()
	return ret

def main():
	obj = req_json('GET', 'graph.facebook.com', '/phmurer')
	print jsonparser.dumps(obj)

if __name__ == '__main__':
	main()
