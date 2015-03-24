package com.murerz.dsopz.oauth;

import org.apache.http.client.fluent.Request;

public abstract class OAuth {

	private static Object MUTEX = new Object();

	private static OAuth me = null;

	public static OAuth me() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					me = new LinkOAuth();
				}
			}
		}
		return me;
	}

	private boolean autoLogin = false;

	public abstract String getToken();

	protected abstract void login();

	public OAuth setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
		return this;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public static void main(String[] args) {
		OAuth oauth = OAuth.me();
		oauth.setAutoLogin(false);
		System.out.println(oauth.getToken());
	}

	public abstract void logout();

	public void config(Request req) {
		String token = getToken();
		if (token == null) {
			throw new RuntimeException("wrong");
		}
		req.setHeader("Authorization", "Bearer " + token);
	}

}
