package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.murerz.base64.Base64.Encoder;

public class Base64EncoderTest {

	@Test
	public void testBase64Encode() {
		Encoder base = new Base64().encoder();
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
		assertEquals("dGVzdA==", new Base64().encoder().done("test".getBytes()));
		assertEquals("bXVyZXIgdGVzdCBhYmNmaW5hbGx5", new Base64().encoder().done("murer test abcfinally".getBytes()));
	}

}
