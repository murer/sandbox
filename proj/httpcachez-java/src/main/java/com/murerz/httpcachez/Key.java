package com.murerz.httpcachez;

public class Key {

	private final String host;
	private final int port;
	private final String path;

	public Key(String host, int port, String path) {
		this.host = host;
		this.port = port;
		this.path = path;
	}

}
