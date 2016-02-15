package com.murerz.httpcachez;

import org.junit.After;
import org.junit.Before;

import com.murerz.httpcachez.server.SimpleHttpServer;

public class AbstractTestCase {

	protected Integer serverPort;
	protected Integer port;
	private ProxyServer proxy;

	@Before
	public void setUp() {
		serverPort = SimpleHttpServer.me().start(0).getPort();
		SimpleHttpServer.me().put("/ping.txt").code(200, "OK").header("Content-Type", "text/plain").content("PONG");
	
		proxy = new ProxyServer();
		port = proxy.start(0).getPort();
		
	}

	@After
	public void tearDown() {
		Util.close(proxy);
		Util.close(SimpleHttpServer.me());
	}

}
