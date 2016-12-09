package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

	@Test
	public void testBase64() {
		Base64 base = new Base64();
		assertEquals("xxx", base.encode("test".getBytes()));
		assertEquals("xxx", new String(base.decode("dGVzdA==")));
	}

}
