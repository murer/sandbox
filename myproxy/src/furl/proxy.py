from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import SocketServer

class Error(Exception):
	"""Exceptions"""

class ProxyHandler(BaseHTTPRequestHandler):

    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def req(self, method):
        self._set_headers()
        self.wfile.write("<html><body><h1>hi!</h1></body></html>")

    def do_GET(self):
        self.req('GET')
    def do_HEAD(self):
        self.req('HEAD')
    def do_PUT(self):
        self.req('PUT')
    def do_POST(self):
        self.req('POST')
    def do_DELETE(self):
        self.req('DELETE')

def run(server_class=HTTPServer, handler_class=ProxyHandler, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print 'Starting httpd...'
    httpd.serve_forever()

class Proxy(object):

    def __init__(self, port=8080, storage='requests'):
        server_address = ('', port)
        self.httpd = HTTPServer(server_address, ProxyHandler)
        self.httpd.furl_storage = storage

    def start(self):
        self.httpd.serve_forever()

def _main():
    p = Proxy()
    p.start()

if __name__ == "__main__":
    _main()
