package com.murerz.httpcachez;

import org.junit.After;
import org.junit.Before;

import com.murerz.httpcachez.server.SimpleHttpServer;

public class AbstractTestCase {

	protected Integer port = null;

	@Before
	public void setUp() {
		port = SimpleHttpServer.me().start(0).getPort();
		SimpleHttpServer.me().put("/ping.txt").code(200, "OK").header("Content-Type", "text/plain").content("PONG");
	}

	@After
	public void tearDown() {
		Util.close(SimpleHttpServer.me());
	}

}
