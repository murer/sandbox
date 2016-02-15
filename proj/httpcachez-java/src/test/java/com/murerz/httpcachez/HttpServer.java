package com.murerz.httpcachez;

import java.io.Closeable;

public class HttpServer implements Closeable {

	private static final HttpServer ME = new HttpServer();

	public static HttpServer me() {
		return ME;
	}

	public HttpServer start(int port) {
		return this;
	}

	public int getPort() {
		return 0;
	}

	@Override
	public void close() {
	}

}
