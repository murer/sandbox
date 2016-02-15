package com.murerz.httpcachez;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HttpServerTest {

	@Test
	public void testHttpServer() {
		HttpServer server = HttpServer.me();
		try {
			int port = server.start(0).getPort();
			assertTrue(port > 0);
		} finally {
			Util.close(server);
		}
	}

}
