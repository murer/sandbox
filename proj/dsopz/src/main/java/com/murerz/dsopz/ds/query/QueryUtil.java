package com.murerz.dsopz.ds.query;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class QueryUtil {

	public static List<String> kinds(String dataset, String namespace) {
		PageQuery query = new PageQuery().setDataset(dataset).setNamespace(namespace).setLimit(1000l).setKind("__kind__");
		JsonArray array = query.query().getEntities();
		if (query.hasMoreElements()) {
			throw new RuntimeException("too many kinds: " + array.size());
		}
		List<String> ret = new ArrayList<String>(array.size());
		for (JsonElement element : array) {
			JsonObject key = element.getAsJsonObject().get("key").getAsJsonObject();
			JsonObject path = key.get("path").getAsJsonArray().get(0).getAsJsonObject();
			String name = path.get("name").getAsString();
			if (!name.startsWith("__")) {
				ret.add(name);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out.println(kinds("quero-natura", "poc"));
	}

}
