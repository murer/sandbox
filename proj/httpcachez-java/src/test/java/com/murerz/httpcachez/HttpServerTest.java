package com.murerz.httpcachez;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HttpServerTest {

	@Test
	public void testHttpServer() {
		HttpServer server = HttpServer.me();
		try {
			server.start(0);
			int port = server.getPort();
			assertTrue(port > 0);
		} finally {
			Util.close(server);
		}
	}

}
