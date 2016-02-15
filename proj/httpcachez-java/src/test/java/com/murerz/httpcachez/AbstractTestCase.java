package com.murerz.httpcachez;

import org.junit.After;
import org.junit.Before;

import com.murerz.httpcachez.server.SimpleHttpServer;

public class AbstractTestCase {

	protected Integer serverPort = null;

	@Before
	public void setUp() {
		serverPort = SimpleHttpServer.me().start(0).getPort();
		SimpleHttpServer.me().put("/ping.txt").code(200, "OK").header("Content-Type", "text/plain").content("PONG");
	
	
	}

	@After
	public void tearDown() {
		Util.close(SimpleHttpServer.me());
	}

}
