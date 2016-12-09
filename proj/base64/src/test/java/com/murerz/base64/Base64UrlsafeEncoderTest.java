package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.murerz.base64.Base64.Encoder;

public class Base64UrlsafeEncoderTest {

	@Test
	public void testBase64Encode() {
		Encoder base = new Base64().urlsafeEncoder();
		assertEquals("dGVz", base.encode("test".getBytes()));
		assertEquals("dA", base.done());

		assertEquals("bXVyZXIgdGVzdCBh", base.encode("murer test abc".getBytes()));
		assertEquals("YmNmaW5hbGx5", base.done("finally".getBytes()));

		assertEquals("bXVyZXIg", base.encode("murer ".getBytes()));
		assertEquals("dGVzdCBh", base.encode("test ab".getBytes()));
		assertEquals("", base.encode("c".getBytes()));
		assertEquals("YmNmaW5hbGx5", base.done("finally".getBytes()));
	}

	@Test
	public void testBase64Done() {
		assertEquals("dGVzdA", new Base64().urlsafeEncoder().done("test".getBytes()));
		assertEquals("bXVyZXIgdGVzdCBhYmNmaW5hbGx5",
				new Base64().urlsafeEncoder().done("murer test abcfinally".getBytes()));
		assertEquals("", new Base64().urlsafeEncoder().done("".getBytes()));

		assertEquals("-_s", new Base64().urlsafeEncoder().done(new byte[] { -5, -5 }));
	}

}
