import oauth_gce
import sys
import http
import json

def get_token():
	return oauth_local.get_token()

def req_json(method, url, params='', headers={}, expects=[200]):
	headers['Authorization'] = 'Bearer %s' % get_token()
	return http.req_json(method, url, params, headers, expects)

def main():
	print get_token()

if __name__ == '__main__':
	main()

