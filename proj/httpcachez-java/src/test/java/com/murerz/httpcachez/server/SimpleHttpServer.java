package com.murerz.httpcachez.server;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpServer implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpServer.class);

	private static final SimpleHttpServer ME = new SimpleHttpServer();

	public static class Resource {

		private int code;
		private String line;
		private final Map<String, String> headers = new HashMap<String, String>();
		private String content;

		public Resource code(int code, String line) {
			this.code = code;
			this.line = line;
			return this;
		}

		public Resource header(String name, String value) {
			headers.put(name, value);
			return this;
		}

		public Resource content(String content) {
			this.content = content;
			return this;
		}

	}

	public static SimpleHttpServer me() {
		return ME;
	}

	private Map<String, Resource> resources;

	private HttpServer server;

	private int port;

	public SimpleHttpServer start(int port) {
		if (server != null) {
			throw new RuntimeException("server already started");
		}
		resources = new HashMap<String, Resource>();
		try {
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(15000).setTcpNoDelay(true).build();

			ServerBootstrap bootstrap = ServerBootstrap.bootstrap();
			bootstrap.setListenerPort(port);
			bootstrap.setServerInfo("Test/1.1");
			bootstrap.setSocketConfig(socketConfig);
			bootstrap.setExceptionLogger(new ExceptionLogger() {
				public void log(Exception e) {
					LOG.error("error", e);
				}
			});
			bootstrap.registerHandler("*", new HttpRequestHandler() {
				public void handle(HttpRequest req, HttpResponse resp, HttpContext ctx)
						throws HttpException, IOException {
					handleRequest(req, resp, ctx);
				}
			});
			this.server = bootstrap.create();

			server.start();
			this.port = server.getLocalPort();
			// server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			return this;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void handleRequest(HttpRequest req, HttpResponse resp, HttpContext ctx) {
		System.out.println("thread: " + Thread.currentThread() + ", req: " + req + ", resp: " + resp + ", ctx: " + ctx);
		String uri = req.getRequestLine().getUri();
		Resource resource = resources.get(uri);
		if (resource == null) {
			resource = new Resource().code(404, "Not Found").header("Content-Type", "text/plain; charset=UTF-8")
					.content("Not Found: " + uri);
		}

		send(resp, resource);
	}

	private void send(HttpResponse resp, Resource content) {
		resp.setStatusCode(content.code);
		resp.setReasonPhrase(content.line);
		for (Entry<String, String> entry : content.headers.entrySet()) {
			resp.addHeader(entry.getKey(), entry.getValue());
		}
		resp.setEntity(new StringEntity(content.content, "UTF-8"));
	}

	public int getPort() {
		return port;
	}

	@Override
	public void close() {
		server.stop();
		server = null;
		resources = null;
		port = 0;
	}

	public Resource put(String name) {
		Resource ret = new Resource();
		resources.put(name, ret);
		return ret;
	}

}
