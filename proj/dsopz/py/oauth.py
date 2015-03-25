import oauth_installed
import sys
import http
import json

def get_token():
	return oauth_installed.get_token()

def oauth_req_json(method, url, params = '', headers = {}, expects = [200]):
	headers['Authorization'] = 'Bearer %s' % get_token()
	return http.req_json(method, url, params, headers, expects)

def __main():
	print get_token()

if __name__ == '__main__':
	__main()

