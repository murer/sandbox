package com.murerz.httpcachez;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpServerTest {

	@Test
	public void testHttpServer() throws Exception, IOException {
		SimpleHttpServer server = SimpleHttpServer.me();
		try {
			server.start(0);
			int port = server.getPort();
			assertTrue(port > 0);

			HttpResponse resp = Request.Get("http://localhost:" + port + "/ping.txt").connectTimeout(10000)
					.socketTimeout(10000).execute().returnResponse();
			assertEquals(404, resp.getStatusLine().getStatusCode());
			assertEquals("Not Found", resp.getStatusLine().getReasonPhrase());
			assertEquals("Not Found: /ping.txt", EntityUtils.toString(resp.getEntity()));

			server.put("/ping.txt").code(200, "OK").header("Content-Type", "text/plain").content("PONG");
			
			resp = Request.Get("http://localhost:" + port + "/ping.txt").connectTimeout(100)
					.socketTimeout(100).execute().returnResponse();
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("OK", resp.getStatusLine().getReasonPhrase());
			assertEquals("PONG", EntityUtils.toString(resp.getEntity()));

		} finally {
			Util.close(server);
		}
	}

}
