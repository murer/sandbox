package com.murerz.dsopz.oauth;

public abstract class OAuth {

	private static Object MUTEX = new Object();

	private static OAuth me = null;

	public static OAuth create() {
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

	private void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public static void main(String[] args) {
		OAuth oauth = OAuth.create();
		oauth.setAutoLogin(false);
		System.out.println(oauth.getToken());
	}

	public abstract void logout();

}
