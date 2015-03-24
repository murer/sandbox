package com.murerz.dsopz.ds.query;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.murerz.dsopz.oauth.OAuth;
import com.murerz.dsopz.util.EntityUtil;
import com.murerz.dsopz.util.GsonUtil;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

public class KindQuery {

	private String dataset;

	private Long limit = 1000l;

	private String endCursor;

	private String moreResults;

	private List<Entity> entities;

	public KindQuery query() {
		InputStream in = null;
		try {
			Request req = Request.Post("https://www.googleapis.com/datastore/v1beta2/datasets/quero-natura/runQuery");
			OAuth.me().config(req);
			JsonObject obj = new JsonObject();
			obj.add("partitionId", new JsonObject());
			req.bodyString("{\"partitionId\":{\"namespace\":\"staging\"},\"query\":{\"limit\":3}}", ContentType.APPLICATION_JSON);
			HttpResponse resp = req.execute().returnResponse();
			HttpUtil.checkError(resp);
			in = new BufferedInputStream(resp.getEntity().getContent());
			JsonObject json = GsonUtil.parse(in, "UTF-8").getAsJsonObject();
			System.out.println(GsonUtil.pretty(json));
			moreResults = json.get("batch").getAsJsonObject().get("moreResults").getAsString();
			endCursor = json.get("batch").getAsJsonObject().get("endCursor").getAsString();
			JsonArray entities = json.get("batch").getAsJsonObject().get("entityResults").getAsJsonArray();
			this.entities = EntityUtil.parseEntities(entities);
			System.out.println(this.entities);
			return this;
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(in);
		}
	}

	public static void main(String[] args) {
		KindQuery query = new KindQuery();
		query.query();
	}

}
