package com.murerz.httpcachez.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.murerz.httpcachez.HTTP;
import com.murerz.httpcachez.Util;

public class HttpServerTest {

	private int port = 0;

	@Test
	public void testHttpServer() throws Exception {
		SimpleHttpServer server = SimpleHttpServer.me();
		try {
			server.start(port);
			port = server.getPort();
			assertTrue(port > 0);

			HttpResponse resp = HTTP.me().get("http://localhost:" + port + "/ping.txt");

			assertEquals(404, resp.getStatusLine().getStatusCode());
			assertEquals("Not Found", resp.getStatusLine().getReasonPhrase());
			assertEquals("Not Found: /ping.txt", EntityUtils.toString(resp.getEntity()));

			server.put("/ping.txt").code(200, "OK").header("Content-Type", "text/plain").content("PONG");

			resp = HTTP.me().get("http://localhost:" + port + "/ping.txt");
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("OK", resp.getStatusLine().getReasonPhrase());
			assertEquals("PONG", EntityUtils.toString(resp.getEntity()));

		} finally {
			Util.close(server);
		}
	}

	@Test
	public void testHttpServerTwice() throws Exception {
		testHttpServer();
	}

}
