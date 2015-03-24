package com.murerz.dsopz.oauth;

import flexjson.JSON;

public class OAuthData {

	private String refreshToken;

	private long createdAt;

	private String authToken;

	private long expiresAt;

	public OAuthData() {
		this.createdAt = System.currentTimeMillis();
	}

	@JSON(name = "refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@JSON(name = "expires_in")
	public Long getExpiresIn() {
		return expiresAt - System.currentTimeMillis();
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresAt = createdAt + expiresIn;
	}

	@JSON(name = "access_token")
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean expired() {
		return System.currentTimeMillis() > expiresAt + 10000;
	}

}
