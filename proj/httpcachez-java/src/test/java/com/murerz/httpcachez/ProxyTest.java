package com.murerz.httpcachez;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

public class ProxyTest extends AbstractTestCase {

	@Test
	public void testProxy() throws Exception {
		HttpResponse resp = HTTP.get("http://localhost:" + serverPort + "/ping.txt");
		assertEquals(200, resp.getStatusLine().getStatusCode());
		assertEquals("OK", resp.getStatusLine().getReasonPhrase());
		assertEquals("PONG", HTTP.toString(resp.getEntity()));
	}

}
