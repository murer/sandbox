package com.murerz.base64;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class Base64InputStreamTest {

	@Test
	public void testBase64Decode() {
		assertEquals("test", decodeString("dGVzdA=="));
		assertEquals("test", decodeString("dGVzdA="));
		assertEquals("test", decodeString("dGVzdA"));
		assertEquals("murer test abcfinally", decodeString("bXVyZXIgdGVzdCBhYmNmaW5hbGx5"));
		assertEquals("", decodeString(""));
		assertEquals("[-5, -5]", Arrays.toString(decode("-_s")));
		assertEquals("[-5, -5]", Arrays.toString(decode("-_s=")));
	}

	private String decodeString(String encoded) {
		return new String(decode(encoded));
	}

	private byte[] decode(String encoded) {
		return Base64.decode(encoded);
	}

	@Test
	public void testBase64LargeDecoder() {
		StringBuilder enc = new StringBuilder();
		enc.append("MQoyCjMKNAo1CjYKNwo4CjkKMTAKMTEKMTIKMTMKMTQKMTUKMTYKMTcKMTgKMTkKMjAKMjEKMjIK\r\n");
		enc.append("\tMjMKMjQKMjUKMjYKMjcKMjgKMjkKMzAKMzEKMzIKMzMKMzQKMzUKMzYKMzcKMzgKMzkKNDAKNDEK\r\n");
		enc.append("NDIKNDMKNDQKNDUKNDYKNDcKNDgKNDkKNTAKNTEKNTIKNTMKNTQKNTUKNTYKNTcKNTgKNTkKNjAK\r\n  ");
		enc.append("NjEKNjIKNjMKNjQKNjUKNjYKNjcKNjgKNjkKNzAKNzEKNzIKNzMKNzQKNzUKNzYKNzcKNzgKNzkK\r\n");
		enc.append("   ODA  KODE  KODIKODMKODQKODUKODYKODcKODgKODkKOTAKOTEKOTIKOTMKOTQKOTUKOTYKOTcKOTgK\n");
		enc.append("OTkKMTAwCg==\n\n\n");
		String nums = decodeString(enc.toString());
		String[] array = nums.split("\\s+");
		for (int i = 0; i < array.length; i++) {
			String num = array[i];
			assertEquals(Integer.toString(i + 1), num);
		}
		assertEquals(100, array.length);

	}

}
