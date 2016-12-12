package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.murerz.base64.Base64.Decoder;

public class Base64DecoderTest {

	@Test
	public void testBase64Decode() {
		Decoder base = Base64.decoder();
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
		assertEquals("test", new String(Base64.decode("dGVzdA")));
		assertEquals("test", new String(Base64.decode("dGVzdA==")));
		assertEquals("murer test abcfinally", new String(Base64.decode("bXVyZXIgdGVzdCBhYmNmaW5hbGx5")));
		assertEquals("", new String(Base64.decode("")));

		assertEquals("[-5, -5]", Arrays.toString(Base64.decode("-_s")));
		assertEquals("[-5, -5]", Arrays.toString(Base64.decode("-_s=")));
	}

	@Test
	public void testBase64LargeDecoder() {
		StringBuilder enc = new StringBuilder();
		enc.append("MQoyCjMKNAo1CjYKNwo4CjkKMTAKMTEKMTIKMTMKMTQKMTUKMTYKMTcKMTgKMTkKMjAKMjEKMjIK\r\n");
		enc.append("MjMKMjQKMjUKMjYKMjcKMjgKMjkKMzAKMzEKMzIKMzMKMzQKMzUKMzYKMzcKMzgKMzkKNDAKNDEK\r\n");
		enc.append("NDIKNDMKNDQKNDUKNDYKNDcKNDgKNDkKNTAKNTEKNTIKNTMKNTQKNTUKNTYKNTcKNTgKNTkKNjAK\r\n  ");
		enc.append("NjEKNjIKNjMKNjQKNjUKNjYKNjcKNjgKNjkKNzAKNzEKNzIKNzMKNzQKNzUKNzYKNzcKNzgKNzkK\r\n");
		enc.append("   ODA  KODE  KODIKODMKODQKODUKODYKODcKODgKODkKOTAKOTEKOTIKOTMKOTQKOTUKOTYKOTcKOTgK\n");
		enc.append("OTkKMTAwCg==\n\n\n");
		System.out.println(enc.toString());
		
		byte[] data = Base64.decode(enc.toString());
		String test = new String(data);
		System.out.println(test);
	}

}
