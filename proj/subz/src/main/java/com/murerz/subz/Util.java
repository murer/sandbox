package com.murerz.subz;

import java.io.File;

public class Util {

	public static String name(File f) {
		return f == null ? null : f.getName();
	}

}
