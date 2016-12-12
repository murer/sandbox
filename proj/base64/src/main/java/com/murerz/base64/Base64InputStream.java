package com.murerz.base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.murerz.base64.Base64.Decoder;

public class Base64InputStream extends InputStream {

	private Decoder decoder;
	private Reader reader;

	private byte[] buffer = new byte[0];
	private int offset = 0;
	private char[] chars = new char[(8 * 1024 * 3) / 4];

	public Base64InputStream(Decoder decoder, Reader reader) {
		this.decoder = decoder;
		this.reader = reader;
	}

	private int load() throws IOException {
		int len = buffer.length - offset;
		if (len > 0) {
			return len;
		}
		if (chars == null) {
			return -1;
		}
		offset = 0;
		int clen = reader.read(chars);
		if (clen < 0) {
			buffer = decoder.done();
			chars = null;
			return buffer.length;
		}
		buffer = decoder.decode(chars, 0, clen);
		return buffer.length;
	}

	@Override
	public int read() throws IOException {
		int len = load();
		while (len == 0) {
			len = load();
		}
		if (len < 0) {
			return len;
		}
		return consumeOne();
	}

	private int consumeOne() {
		return buffer[offset++];
	}

}
