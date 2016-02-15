package com.murerz.httpcachez;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger LOG = LoggerFactory.getLogger(Util.class);

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
				LOG.error("error closing", e);
			}
		}
	}

}
