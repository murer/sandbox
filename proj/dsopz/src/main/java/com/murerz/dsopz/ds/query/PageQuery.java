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
import com.murerz.dsopz.util.GsonUtil;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

public class PageQuery {

	private String dataset;

	private String namespace;

	private Long limit = 10l;

	private String endCursor;

	private String startCursor;

	private String moreResults;

	private JsonArray entities;

	public void setStartCursor(String startCursor) {
		this.startCursor = startCursor;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public PageQuery query() {
		InputStream in = null;
		try {
			Request req = Request.Post("https://www.googleapis.com/datastore/v1beta2/datasets/" + dataset + "/runQuery");
			OAuth.me().config(req);
			JsonObject params = createParams();
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
			JsonObject props = obj.get("properties").getAsJsonObject();
			entity.add("properties", parseProps(props));
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

	private JsonObject createParams() {
		JsonObject ret = new JsonObject();
		JsonObject partitionId = new JsonObject();
		JsonObject query = new JsonObject();
		ret.add("partitionId", partitionId);
		ret.add("query", query);
		partitionId.addProperty("namespace", namespace);
		query.addProperty("limit", limit);
		if (startCursor != null) {
			query.addProperty("startCursor", startCursor);
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
		return ("MORE_RESULTS_AFTER_LIMIT".equals(moreResults));
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
			System.out.println("xxxx");
			return false;
		}
		startCursor = endCursor;
		endCursor = null;
		moreResults = null;
		return true;

	}

}
