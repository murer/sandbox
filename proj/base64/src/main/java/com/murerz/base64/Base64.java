package com.murerz.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64 {

	private final static char[] ALPHABET_ORIGINAL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();
	private final static char[] ALPHABET_URLSAFE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
			.toCharArray();

	public static class Encoder {

		protected char[] alphabet = ALPHABET_ORIGINAL;

		protected byte[] buffer = new byte[0];

		public String encode(byte[] data) {
			int i = 0;
			StringBuilder ret = new StringBuilder();

			if (buffer.length + data.length < 3) {
				buffer = concat(buffer, data);
				return ret.toString();
			}

			if (buffer.length == 1) {
				ret.append(word(buffer[0], data[i++], data[i++]));
			} else if (buffer.length == 2) {
				ret.append(word(buffer[0], buffer[1], data[i++]));
			} else if (buffer.length != 0) {
				throw new RuntimeException("wrong: " + buffer.length);
			}
			buffer = new byte[0];

			while (i <= data.length - 3) {
				ret.append(word(data[i++], data[i++], data[i++]));
			}
			if (i < data.length - 1) {
				this.buffer = new byte[] { data[i++], data[i++] };
			} else if (i < data.length) {
				this.buffer = new byte[] { data[i++] };
			}
			if (i != data.length) {
				throw new RuntimeException("wrong: " + i);
			}
			return ret.toString();
		}

		protected char[] word(int b1, int b2, int b3) {
			b1 = 0x000000FF & b1;
			b2 = 0x000000FF & b2;
			b3 = 0x000000FF & b3;
			System.out.println(String.format("word = [ %s, %s, %s ]", b1, b2, b3));
			System.out.println(String.format("bin = [ %s, %s, %s ]", Integer.toBinaryString(b1),
					Integer.toBinaryString(b2), Integer.toBinaryString(b3)));
			int r1 = b1 >> 2;
			int r2 = (b1 << 4) | (b2 >> 4);
			int r3 = (b2 << 2) | (b3 >> 6);
			int r4 = b3 & 0x0000003F;
			r1 = 0x0000003F & r1;
			r2 = 0x0000003F & r2;
			r3 = 0x0000003F & r3;
			System.out.println(String.format("rs = [ %s, %s, %s, %s ]", r1, r2, r3, r4));
			System.out.println(String.format("rbin = [ %s, %s, %s, %s ]", Integer.toBinaryString(r1),
					Integer.toBinaryString(r2), Integer.toBinaryString(r3), Integer.toBinaryString(r4)));
			char c1 = alphabet[r1];
			char c2 = alphabet[r2];
			char c3 = alphabet[r3];
			char c4 = alphabet[r4];
			System.out.println(String.format("chars = [ %s, %s, %s, %s ]", c1, c2, c3, c4));
			return new char[] { c1, c2, c3, c4 };
		}

		public String done() {
			char[] ret = new char[0];
			if (buffer.length == 1) {
				ret = word(buffer[0], 0, 0);
				ret[2] = '=';
				ret[3] = '=';
			} else if (buffer.length == 1) {
				ret = word(buffer[0], buffer[1], 0);
				ret[3] = '=';
			} else if (buffer.length != 0) {
				throw new RuntimeException("wrong: " + buffer.length);
			}
			this.buffer = new byte[0];
			return new String(ret);
		}

		public String done(byte[] data) {
			StringBuilder ret = new StringBuilder();
			ret.append(encode(data));
			ret.append(done());
			return ret.toString();
		}

	}

	public static class UrlsafeEncoder extends Encoder {
		public UrlsafeEncoder() {
			alphabet = ALPHABET_URLSAFE;
		}

		@Override
		public String done() {
			char[] ret = new char[0];
			if (buffer.length == 1) {
				ret = word(buffer[0], 0, 0);
				ret = new char[] { ret[0], ret[1] };
			} else if (buffer.length == 1) {
				ret = word(buffer[0], buffer[1], 0);
				ret = new char[] { ret[0], ret[1], ret[2] };
			} else if (buffer.length != 0) {
				throw new RuntimeException("wrong: " + buffer.length);
			}
			this.buffer = new byte[0];
			return new String(ret);
		}
	}

	public static class Decoder {

		protected char[] alphabet = ALPHABET_ORIGINAL;

		private StringBuilder buffer = new StringBuilder();

		public byte[] decode(String encoded) {
			try {
				char[] chars = encoded.toCharArray();

				int i = 0;
				ByteArrayOutputStream ret = new ByteArrayOutputStream();

				if (buffer.length() + chars.length < 4) {
					buffer = buffer.append(chars);
					ret.close();
					return ret.toByteArray();
				}

				if (buffer.length() == 1) {
					ret.write(word(buffer.charAt(0), chars[i++], chars[i++], chars[i++]));
				} else if (buffer.length() == 2) {
					ret.write(word(buffer.charAt(0), buffer.charAt(1), chars[i++], chars[i++]));
				} else if (buffer.length() == 3) {
					ret.write(word(buffer.charAt(0), buffer.charAt(1), buffer.charAt(2), chars[i++]));
				} else if (buffer.length() != 0) {
					throw new RuntimeException("wrong: " + buffer);
				}
				buffer = new StringBuilder();

				while (i <= chars.length - 4) {
					ret.write(word(chars[i++], chars[i++], chars[i++], chars[i++]));
				}
				this.buffer = new StringBuilder().append(chars, i, chars.length - i);
				if (buffer.length() >= 4) {
					throw new RuntimeException("wrong: " + buffer);
				}
				ret.close();
				return ret.toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		protected byte[] word(char c1, char c2, char c3, char c4) {
			System.out.println(String.format("word = [ %s, %s, %s, %s ]", c1, c2, c3, c4));
			int i1 = c1 == '=' ? 0 : indexOf(c1);
			int i2 = c2 == '=' ? 0 : indexOf(c2);
			int i3 = c3 == '=' ? 0 : indexOf(c3);
			int i4 = c4 == '=' ? 0 : indexOf(c4);
			System.out.println(String.format("dec = [ %s, %s, %s, %s ]", i1, i2, i3, i4));
			System.out.println(String.format("bin = [ %s, %s, %s, %s ]", Integer.toBinaryString(i1),
					Integer.toBinaryString(i2), Integer.toBinaryString(i3), Integer.toBinaryString(i4)));
			int r1 = (i1 << 2) | (i2 >> 4);
			int r2 = (i2 << 4) | (i3 >> 2);
			int r3 = (i3 << 6) | i4;
			r1 = 0x000000FF & r1;
			r2 = 0x000000FF & r2;
			r3 = 0x000000FF & r3;
			System.out.println(String.format("rdec = [ %s, %s, %s ]", r1, r2, r3));
			System.out.println(String.format("rbin = [ %s, %s, %s ]", Integer.toBinaryString(r1),
					Integer.toBinaryString(r2), Integer.toBinaryString(r3)));
			if (c2 == '=') {
				throw new RuntimeException("wrong");
			} else if (c3 == '=') {
				return new byte[] { (byte) r1 };
			} else if (c4 == '=') {
				return new byte[] { (byte) r1, (byte) r2 };
			}
			return new byte[] { (byte) r1, (byte) r2, (byte) r3 };
		}

		private int indexOf(char ch) {
			for (int i = 0; i < ALPHABET_ORIGINAL.length; i++) {
				char c = ALPHABET_ORIGINAL[i];
				if (c == ch) {
					return i;
				}
			}
			throw new RuntimeException("not found: " + ch);
		}

		public byte[] done(String encoded) {
			try {
				ByteArrayOutputStream ret = new ByteArrayOutputStream();
				ret.write(decode(encoded));
				ret.write(done());
				ret.close();
				return ret.toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public byte[] done() {
			StringBuilder sb = new StringBuilder(this.buffer);
			this.buffer = new StringBuilder();
			if (sb.length() == 0) {
				return new byte[0];
			}
			while (sb.length() < 4) {
				sb.append('=');
			}
			return word(sb.charAt(0), sb.charAt(1), sb.charAt(2), sb.charAt(3));

		}

	}

	protected static byte[] concat(byte[] a1, byte[] a2) {
		byte[] ret = new byte[a1.length + a2.length];
		System.arraycopy(a1, 0, ret, 0, a1.length);
		System.arraycopy(a2, 0, ret, a1.length, a2.length);
		return ret;
	}

	public Encoder encoder() {
		return new Encoder();
	}

	public Encoder urlsafeEncoder() {
		return new UrlsafeEncoder();
	}

	public Decoder decoder() {
		return new Decoder();
	}
}
