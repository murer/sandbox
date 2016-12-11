package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.murerz.base64.Base64.Decoder;

public class Base64DecoderTest {

	@Test
	public void testBase64Decode() {
		Decoder base = new Base64().decoder();
		assertEquals("tes", new String(base.decode("dGVz")));
		assertEquals("t", new String(base.done("dA==")));

		assertEquals("murer test a", new String(base.decode("bXVyZXIgdGVzdCBh")));
		assertEquals("bcfinally", new String(base.done("YmNmaW5hbGx5")));

		assertEquals("murer ", new String(base.decode("bXVyZXIg")));
		assertEquals("test a", new String(base.decode("dGVzdCBh")));
		assertEquals("", new String(base.decode("")));
		assertEquals("bcfinally", new String(base.done("YmNmaW5hbGx5")));
	}

	@Test
	public void testBase64Done() {
		assertEquals("test", new String(new Base64().decoder().done("dGVzdA")));
		assertEquals("test", new String(new Base64().decoder().done("dGVzdA==")));
		assertEquals("murer test abcfinally",
				new String(new Base64().decoder().decode("bXVyZXIgdGVzdCBhYmNmaW5hbGx5")));
		assertEquals("", new String(new Base64().decoder().done("")));
		
		assertEquals("[-5, -5]", Arrays.toString(new Base64().decoder().done("-_s")));
		assertEquals("[-5, -5]", Arrays.toString(new Base64().decoder().done("-_s=")));
	}

}
