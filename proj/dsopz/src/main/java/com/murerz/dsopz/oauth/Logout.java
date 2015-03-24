package com.murerz.dsopz.oauth;

public class Logout {

	public static void main(String[] args) {
		OAuth oauth = OAuth.me();
		oauth.logout();
	}

}
