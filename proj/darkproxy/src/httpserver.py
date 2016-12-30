import SocketServer
import BaseHTTPServer
import SimpleHTTPServer

class ThreadingSimpleServer(SocketServer.ThreadingMixIn, BaseHTTPServer.HTTPServer):
    pass

class _Handler(BaseHTTPServer.BaseHTTPRequestHandler):

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


class Server(object):

    def __init__(self, port, handler):
        self.port = port
        self.handler = handler

    def start(self):
        self.server = ThreadingSimpleServer(('', self.port), _Handler)

    def serve(self):
        self.server.serve_forever()

    def stop(self):
        """ implement it """
