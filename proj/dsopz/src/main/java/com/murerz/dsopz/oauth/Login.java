package com.murerz.dsopz.oauth;

public class Login {

	public static void main(String[] args) {
		OAuth oauth = OAuth.create();
		oauth.login();
		System.out.println(oauth.getToken());
	}

}
