package com.murerz.dsopz.oauth;

public abstract class OAuth {

	private static Object MUTEX = new Object();

	private static OAuth me = null;

	private static OAuth create() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					me = new LinkOAuth();
				}
			}
		}
		return me;
	}

	public abstract String getToken();

	protected abstract void login();

	public static void main(String[] args) {
		OAuth oauth = OAuth.create();
		oauth.login();
		System.out.println(oauth.getToken());
	}

}
