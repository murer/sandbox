package com.murerz.dsopz.oauth;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.murerz.dsopz.util.Console;
import com.murerz.dsopz.util.FlexJson;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

public class LinkOAuth extends OAuth {

	private OAuthData data;

	@Override
	public String getToken() {
		readConfigFile();
		if (expired()) {
			makeLogin();
		}
		return data.getAuthToken();
	}

	private void makeLogin() {
		try {
			String clientId = "765762103246-g936peorj64mgveoqhai6ohv4t5qc5qb.apps.googleusercontent.com";
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
			String code = Console.me().readln();

			Request req = Request.Post("https://www.googleapis.com:443/oauth2/v3/token");
			Form form = Form.form();
			form.add("code", code).add("client_id", clientId);
			form.add("client_secret", "ayQpUnTqvIxgV1XY9e-ItyC8");
			form.add("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
			form.add("grant_type", "authorization_code");
			req.bodyForm(form.build());
			HttpResponse resp = req.execute().returnResponse();
			HttpUtil.checkError(resp);
			OAuthData data = HttpUtil.jsonFlex(resp, OAuthData.class);
			File file = getFile();
			FlexJson.writeFile(file, data);
			this.data = data;
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean expired() {
		if (data == null) {
			return true;
		}
		return data.expired();
	}

	private void readConfigFile() {
		File file = getFile();
		data = FlexJson.parseFile(file, OAuthData.class);
	}

	@Override
	protected void login() {
		deleteConfigFile();
		getToken();
	}

	private void deleteConfigFile() {
		File file = getFile();
		file.delete();
	}

	private File getFile() {
		String file = System.getProperty("dsopz.oauth.file");
		if (file == null) {
			file = System.getProperty("user.home", ".") + "/.dsopz/auth.json";
		}
		return new File(file);
	}

}
