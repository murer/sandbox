import SocketServer
import BaseHTTPServer
import SimpleHTTPServer

class ThreadingSimpleServer(SocketServer.ThreadingMixIn, BaseHTTPServer.HTTPServer):

    def req(self, req):
        self.server.req(req)

class RequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):

    def req(self, method):
        self.server.req(self)

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

    def __init__(self, handler, port=8000):
        self.port = port
        self.handler = handler

    def serve(self):
        self.simple_server = ThreadingSimpleServer(('', self.port), RequestHandler)
        self.simple_server.server = self
        self.simple_server.serve_forever()

    def req(self, req):
        self.handler.req(req)

class Handler(object):

    def req(self, req):
        """ implement it """

class SimpleHandler(object):

    def req(self, req):
        req.send_response(200)
        req.send_header('Content-type', 'text/plain; charset=UTF-8')
        req.end_headers()
        req.wfile.write("OK")

def _main():
    proxy = Server(SimpleHandler(), port=8000)
    proxy.serve()

if __name__ == "__main__":
    _main()
