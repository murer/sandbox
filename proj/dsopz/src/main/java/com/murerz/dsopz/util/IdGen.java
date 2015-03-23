package com.murerz.dsopz.util;

import java.util.UUID;

public class IdGen {

	public static String gen(String type) {
		type = type.toLowerCase();
		return UUID.randomUUID().toString();
	}

	public static String gen(Class<?> clazz) {
		return gen(clazz.getSimpleName().toLowerCase());
	}
}
