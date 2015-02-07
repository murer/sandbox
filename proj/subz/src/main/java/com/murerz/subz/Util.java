package com.murerz.subz;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

public class Util {

	public static String name(File f) {
		return f == null ? null : f.getName();
	}

	public static String name(SubzFile file) {
		return file == null ? null : name(file.getFile());
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static long mega(long length) {
		return length / (1024 * 1024);
	}

	public static String human(long length) {
		BigDecimal ret = new BigDecimal(length).divide(new BigDecimal(1024 ^ 3), 2, BigDecimal.ROUND_HALF_UP);
		if (ret.compareTo(new BigDecimal(1l)) > 0) {
			return "" + ret + "g";
		}
		ret = new BigDecimal(length).divide(new BigDecimal(1024 ^ 2), 2, BigDecimal.ROUND_HALF_UP);
		if (ret.compareTo(new BigDecimal(1l)) > 0) {
			return "" + ret + "m";
		}
		ret = new BigDecimal(length).divide(new BigDecimal(1024 ^ 1), 2, BigDecimal.ROUND_HALF_UP);
		if (ret.compareTo(new BigDecimal(1l)) > 0) {
			return "" + ret + "k";
		}
		return "" + length + "b";
	}

	public static String human(SubzFile file) {
		return file == null ? "null" : human(file.getFile());
	}

	private static String human(File file) {
		return file == null ? "null" : human(file.length());
	}

	public static void copy(File from, File to) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			copyAll(in, out);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			close(in);
			close(out);
		}
	}

	public static void copyAll(InputStream in, OutputStream out) {
		try {
			byte[] buffer = new byte[1024 * 8];
			int read = 0;
			do {
				read = in.read(buffer);
				if (read > 0) {
					out.write(buffer, 0, read);
				}
			} while (read >= 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
