package com.murerz.httpcachez;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

public class HTTP {

	public static HttpResponse get(String url) {
		try {
			return create(url).execute().returnResponse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Request create(String url) {
		return Request.Get(url).connectTimeout(100000).socketTimeout(100000);
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

	public static HttpResponse getWithHost(String url, String host) {
		try {
			return create(url).setHeader("Host", host).execute()
					.returnResponse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
