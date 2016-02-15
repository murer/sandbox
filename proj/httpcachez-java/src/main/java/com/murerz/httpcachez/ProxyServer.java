package com.murerz.httpcachez;

import java.io.Closeable;
import java.io.IOException;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyServer implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(ProxyServer.class);
	private HttpServer server;
	private int port;

	public ProxyServer start(int port) {
		try {
			if (server != null) {
				throw new RuntimeException("server already started");
			}
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
			return this;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void handleRequest(HttpRequest req, HttpResponse resp, HttpContext ctx) {
		Key key = key(req);
		Job job = Job.get(key);
		job.execute(req, resp);
	}

	private Key key(HttpRequest req) {
		String host = HTTP.header(req, "TargetHost");
		if (host == null) {
			host = HTTP.header(req, "Host");
		}
		if (host == null) {
			throw new RuntimeException("Host header is required");
		}
		if (host.indexOf(":") < 0) {
			host += ":80";
		}
		String[] array = host.split(":");
		Key key = new Key(array[0], Integer.parseInt(array[1]), req.getRequestLine().getUri());
		return key;
	}

	public Integer getPort() {
		return port;
	}

	@Override
	public void close() throws IOException {
		server.stop();
		server = null;
		port = 0;
	}

}
