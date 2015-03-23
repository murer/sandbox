package com.murerz.dsopz.oauth;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.murerz.dsopz.util.Console;
import com.murerz.dsopz.util.FlexJson;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

import flexjson.JSON;

public class LinkLogin {

	private static class Data {

		private String refreshToken;

		private long createdAt;

		private String authToken;

		private long expiresAt;

		public Data() {
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

	}

	private File file;

	private Data data;

	private String clientId = "765762103246-g936peorj64mgveoqhai6ohv4t5qc5qb.apps.googleusercontent.com";

	private LinkLogin file(String file) {
		this.file = new File(file);
		data = FlexJson.parseFile(this.file, Data.class);
		return this;
	}

	private LinkLogin login() {
		try {
			List<String> scopes = new ArrayList<String>();
			scopes.add("https://www.googleapis.com/auth/cloud-platform");
			scopes.add("https://www.googleapis.com/auth/datastore");
			scopes.add("https://www.googleapis.com/auth/userinfo.email");

			URIBuilder uri = new URIBuilder("https://accounts.google.com/o/oauth2/auth");
			uri.addParameter("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
			uri.addParameter("response_type", "code");
			uri.addParameter("client_id", clientId);
			uri.addParameter("scope", Util.joinColl(" ", scopes));
			uri.addParameter("approval_prompt", "force");
			uri.addParameter("access_type", "offline");

			System.out.println("Open this url:");
			System.out.println(uri.build());
			System.out.println("Code: ");
			String line = Console.me().readln();
			System.out.println(line);

			this.data = exchangeToken(line);
			FlexJson.writeFile(file, this.data);
			return this;
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private Data exchangeToken(String code) {
		try {
			Request req = Request.Post("https://www.googleapis.com:443/oauth2/v3/token");
			Form form = Form.form();
			form.add("code", code).add("client_id", clientId);
			form.add("client_secret", "ayQpUnTqvIxgV1XY9e-ItyC8");
			form.add("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
			form.add("grant_type", "authorization_code");
			req.bodyForm(form.build());
			HttpResponse resp = req.execute().returnResponse();
			HttpUtil.checkError(resp);
			Data data = HttpUtil.jsonFlex(resp, Data.class);
			return data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		boolean force = "true".equals(System.getProperty("dspoz.login.force", "true"));
		String file = System.getProperty("dsopz.login.file");
		if (file == null) {
			file = System.getProperty("user.home", ".") + "/.dsopz/login.json";
		}
		LinkLogin login = new LinkLogin();
		login.file(file);
		if (force || !login.file.exists()) {
			login.login();
		}
	}

}
