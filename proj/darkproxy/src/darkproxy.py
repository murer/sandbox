import util
import httpserver

class DarkProxy(object):

    def __init__(self, port=8000):
        self.port = port
        self.server = None

    def start(self):
        if self.server:
            raise util.Error('Server is started')
        self.server = httpserver.Server(self.port, self)
        self.server.start()

    def serve(self):
        self.server.serve()

    def stop(self):
        """ implement it """
