package com.murerz.cryptguide;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Dec {

	public static void main(String[] args) throws Exception {
		String alg = "AES/CBC/PKCS5Padding";
		byte[] chipertext = Codec.b64("Yd32Tkvopk1qqjdAnn+RKQ==");
		byte[] encodedkey = Codec.hex("7d61bb87ebe736b23e9985f76481a785");
		byte[] iv = Codec.hex("00112233445566778899AABBCCDDEEFF");

		System.out.println(String.format("Alg: %s", alg));

		SecretKeySpec key = new SecretKeySpec(encodedkey, alg.split("/")[0]);
		System.out.println(
				String.format("Parsed Key: %s, len: %s", Codec.hex(key.getEncoded()), key.getEncoded().length));
		System.out.println(String.format("IV: %s, len: %s", Codec.hex(iv), iv.length));

		System.out.println(String.format("Chipertext: %s, len: %s", Codec.hex(chipertext), chipertext.length));

		Cipher dec = Cipher.getInstance(alg);
		dec.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		byte[] plaintext = dec.doFinal(chipertext);
		System.out.println(String.format("Plaintext: %s, len: %s, str: %s", Codec.hex(plaintext), plaintext.length,
				new String(plaintext, "UTF-8")));
	}

}
