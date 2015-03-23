package com.murerz.dsopz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Console {

	private static final Object MUTEX = new Object();

	private static Console me = null;

	private BufferedReader in;

	private PrintStream out;

	public static Console me() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					Console ret = new Console();
					ret.prepare();
					me = ret;
				}
			}
		}
		return me;
	}

	private void prepare() {
		this.in = new BufferedReader(new InputStreamReader(System.in));
		this.out = System.out;
	}

	public String readln() {
		try {
			return in.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
