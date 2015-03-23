package com.murerz.dsopz.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

public class HttpUtil {

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

	public static void checkError(HttpResponse resp) {
		int code = resp.getStatusLine().getStatusCode();
		if (code != 200) {
			String msg = null;
			try {
				msg = EntityUtils.toString(resp.getEntity());
			} catch (IOException e) {
				LOG.error("error", e);
			}
			throw new RuntimeException("error: " + code + " " + FlexJson.instance().format(msg));
		}
	}

	public static JsonElement json(HttpResponse resp) {
		try {
			String ret = EntityUtils.toString(resp.getEntity());
			return GsonUtil.parse(ret);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T jsonFlex(HttpResponse resp, Class<T> clazz) {
		try {
			String ret = EntityUtils.toString(resp.getEntity());
			return FlexJson.instance().parse(ret, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
