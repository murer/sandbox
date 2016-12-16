package com.murerz.cryptguide;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Key {

	public static void main(String[] args) throws Exception {
		String alg = "AES";
		int keylen = 128;

		System.out.println(String.format("Alg: %s", alg));
		KeyGenerator keygen = KeyGenerator.getInstance(alg);
		keygen.init(keylen);
		SecretKey key = keygen.generateKey();
		System.out.println(
				String.format("Generated Key: %s, len: %s", Codec.hex(key.getEncoded()), key.getEncoded().length));

	}

}
