package com.murerz.httpcachez;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

public class HTTP {

	public static HttpResponse get(String url) {
		try {
			return Request.Get(url).connectTimeout(100).socketTimeout(100).execute().returnResponse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toString(HttpEntity entity) {
		try {
			return EntityUtils.toString(entity);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
