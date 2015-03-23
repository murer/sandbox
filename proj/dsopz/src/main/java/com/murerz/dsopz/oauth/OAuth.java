package com.murerz.dsopz.oauth;

public class OAuth {

	private static Object MUTEX = new Object();

	private static OAuth me = null;

	private static OAuth create() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					me = new OAuth();
				}
			}
		}
		return me;
	}

	public String getToken() {
		return null;
	}

	public static void main(String[] args) {
		OAuth oauth = OAuth.create();
		System.out.println(oauth.getToken());
	}

}
