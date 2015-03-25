package com.murerz.dsopz.ds.query;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.murerz.dsopz.oauth.OAuth;
import com.murerz.dsopz.util.FlexJson;
import com.murerz.dsopz.util.GsonUtil;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

public class PageQuery {

	private String dataset;

	private QueryParam params = new QueryParam();

	private String endCursor;

	private String moreResults;

	private JsonArray entities;

	public PageQuery setStartCursor(String startCursor) {
		this.params.setStartCursor(startCursor);
		return this;
	}

	public PageQuery setDataset(String dataset) {
		this.dataset = dataset;
		return this;
	}

	public PageQuery setNamespace(String namespace) {
		this.params.namespace(namespace);
		return this;
	}

	public PageQuery setLimit(Long limit) {
		this.params.limit(limit);
		return this;
	}

	public PageQuery query() {
		InputStream in = null;
		try {
			Request req = Request.Post("https://www.googleapis.com/datastore/v1beta2/datasets/" + dataset + "/runQuery");
			OAuth.me().config(req);
			String params = FlexJson.toJSON(this.params);
			req.bodyString(params.toString(), ContentType.APPLICATION_JSON);
			HttpResponse resp = req.execute().returnResponse();
			HttpUtil.checkError(resp);
			in = new BufferedInputStream(resp.getEntity().getContent());
			JsonObject json = GsonUtil.parse(in, "UTF-8").getAsJsonObject();
			moreResults = json.get("batch").getAsJsonObject().get("moreResults").getAsString();
			endCursor = json.get("batch").getAsJsonObject().get("endCursor").getAsString();
			JsonArray entities = json.get("batch").getAsJsonObject().get("entityResults").getAsJsonArray();
			this.entities = parseEntities(entities);
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

	private JsonArray parseEntities(JsonArray entities) {
		JsonArray ret = new JsonArray();
		for (JsonElement element : entities) {
			JsonObject obj = element.getAsJsonObject().get("entity").getAsJsonObject();
			JsonObject entity = new JsonObject();
			JsonObject key = new JsonObject();
			JsonArray path = obj.get("key").getAsJsonObject().get("path").getAsJsonArray();
			entity.add("key", key);
			key.add("path", path);
			if (obj.has("properties")) {
				JsonObject props = obj.get("properties").getAsJsonObject();
				entity.add("properties", parseProps(props));
			}
			ret.add(entity);
		}
		return ret;
	}

	private JsonObject parseProps(JsonObject props) {
		JsonObject ret = new JsonObject();
		for (Entry<String, JsonElement> entry : props.entrySet()) {
			JsonObject value = entry.getValue().getAsJsonObject();
			if (!value.has("indexed")) {
				value.addProperty("indexed", false);
			}
			if (value.entrySet().size() > 1) {
				ret.add(entry.getKey(), value);
			}
		}
		return ret;
	}

	public JsonArray getEntities() {
		return entities;
	}

	public String getEndCursor() {
		return endCursor;
	}

	public String getMoreResults() {
		return moreResults;
	}

	public boolean hasMoreElements() {
		return entities.size() < params.getQuery().getLimit();
	}

	public PageQuery setKind(String kind) {
		this.params.kind(kind);
		return this;
	}

	public static void main(String[] args) {
		PageQuery query = new PageQuery();
		query.setDataset("quero-natura");
		query.setNamespace("staging");
		query.query();
		System.out.println(query.getEntities());
		System.out.println(query.getEndCursor());
		System.out.println(query.getMoreResults());
		if (query.nextPage()) {
			query.query();
			System.out.println(query.getEntities());
			System.out.println(query.getEndCursor());
			System.out.println(query.getMoreResults());
		}
	}

	public boolean nextPage() {
		if (!hasMoreElements()) {
			return false;
		}
		setStartCursor(endCursor);
		endCursor = null;
		moreResults = null;
		return true;

	}

	public QueryParam getParams() {
		return params;
	}

}
