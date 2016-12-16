package com.murerz.cryptguide;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Enc {

	public static void main(String[] args) throws Exception {
		String alg = "AES/CBC/PKCS5Padding";
		byte[] plaintext = "murer".getBytes("UTF-8");
		byte[] encodedkey = Codec.hex("7d61bb87ebe736b23e9985f76481a785");
		byte[] iv = Codec.hex("00112233445566778899AABBCCDDEEFF");

		System.out.println(String.format("Alg: %s", alg));

		SecretKeySpec key = new SecretKeySpec(encodedkey, alg.split("/")[0]);
		System.out.println(
				String.format("Parsed Key: %s, len: %s", Codec.hex(key.getEncoded()), key.getEncoded().length));
		System.out.println(
				String.format("IV: %s, len: %s", Codec.hex(iv), iv.length));

		System.out.println(String.format("Plaintext: %s, len: %s, str: %s", Codec.hex(plaintext), plaintext.length,
				new String(plaintext, "UTF-8")));

		Cipher enc = Cipher.getInstance(alg);
		enc.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
		byte[] chipertext = enc.doFinal(plaintext);
		System.out.println(String.format("Chipertext: %s, len: %s", Codec.b64(chipertext), chipertext.length));
		
	}

}
