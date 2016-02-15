package com.murerz.httpcachez;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HTTP {

	private static final Object MUTEX = new Object();
	private static HTTP me;

	public static HTTP me() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					HTTP ret = new HTTP();
					ret.init();
					me = ret;
				}
			}
		}
		return me;
	}

	private void init() {
	}

	public HttpResponse get(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = null;
		try {
			HttpGet req = new HttpGet(url);
			resp = client.execute(req);
			String str = EntityUtils.toString(resp.getEntity());
			EntityUtils.updateEntity(resp, new StringEntity(str));
			return resp;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(resp);
			Util.close(client);
		}
	}

	public HttpResponse getWithHost(String url, String host) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = null;
		try {
			HttpGet req = new HttpGet(url);
			req.setHeader("Host", host);
			resp = client.execute(req);
			String str = EntityUtils.toString(resp.getEntity());
			EntityUtils.updateEntity(resp, new StringEntity(str));
			return resp;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(resp);
			Util.close(client);
		}
	}

	public static String toString(HttpEntity entity) {
		try {
			return EntityUtils.toString(entity);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String header(HttpRequest req, String name) {
		Header header = req.getFirstHeader(name);
		if (header == null) {
			return null;
		}
		return header.getValue();
	}

	public void proxy(String url, HttpResponse response) {
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse resp = null;
		try {
			HttpGet req = new HttpGet(url);
			resp = client.execute(req);
			response.setHeader(resp.getFirstHeader("Content-Type"));
			response.setEntity(resp.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(resp);
			Util.close(client);
		}
	}

}
