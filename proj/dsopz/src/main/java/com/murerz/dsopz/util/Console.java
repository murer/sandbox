package com.murerz.dsopz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.commons.lang3.StringEscapeUtils;

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

	public void println(Object obj) {
		System.out.println(obj);
	}

	public void errPrintln(Object obj) {
		System.err.println(obj);
	}

	private static void csv(String s) {
		System.out.println("'" + s + "': ---" + StringEscapeUtils.escapeCsv(s) + "---");
	}

	public static void main(String[] args) {
		csv("abc");
		csv("abc\"de\"f");
		csv("abc,def");
		csv("abc,d\"e\"f");
		csv("abc\"\"\"def");
	}

}
