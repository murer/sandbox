package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

	@Test
	public void testBase64Encode() {
		Base64Encoder base = new Base64Encoder();
		assertEquals("dGVz", base.encode("test".getBytes()));
		assertEquals("dA==", base.done());

		assertEquals("bXVyZXIgdGVzdCBh", base.encode("murer test abc".getBytes()));
		assertEquals("YmNmaW5hbGx5", base.done("finally".getBytes()));

		assertEquals("bXVyZXIg", base.encode("murer ".getBytes()));
		assertEquals("dGVzdCBh", base.encode("test ab".getBytes()));
		assertEquals("", base.encode("c".getBytes()));
		assertEquals("YmNmaW5hbGx5", base.done("finally".getBytes()));
	}

	@Test
	public void testBase64Done() {
		assertEquals("dGVzdA==", new Base64Encoder().done("test".getBytes()));
		assertEquals("bXVyZXIgdGVzdCBhYmNmaW5hbGx5", new Base64Encoder().done("murer test abcfinally".getBytes()));
	}

}
