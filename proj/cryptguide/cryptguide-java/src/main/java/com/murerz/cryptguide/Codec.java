package com.murerz.cryptguide;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Codec {

	public static String hex(byte[] data) {
		return Hex.encodeHexString(data);
	}

	public static byte[] hex(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	public static String b64(byte[] data) {
		return new Base64(-1, null, false).encodeToString(data);
	}

	public static byte[] b64(String data) {
		return new Base64(-1, null, false).decode(data);
	}
}
