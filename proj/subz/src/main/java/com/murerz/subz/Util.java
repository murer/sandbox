package com.murerz.subz;

import java.io.File;
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
}
