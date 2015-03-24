package com.murerz.dsopz.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.murerz.dsopz.ds.query.Entity;
import com.murerz.dsopz.ds.query.Property;

public class EntityUtil {

	public static List<Entity> parseEntities(JsonArray entities) {
		ArrayList<Entity> ret = new ArrayList<Entity>(entities.size());
		for (JsonElement element : entities) {
			JsonObject obj = element.getAsJsonObject().get("entity").getAsJsonObject();
			Entity entity = parseEntity(obj);
			ret.add(entity);
		}
		return ret;
	}

	public static Entity parseEntity(JsonObject obj) {
		Entity entity = new Entity();
		String id = parseId(obj.get("key").getAsJsonObject().get("path").getAsJsonArray());
		entity.setId(id);
		Map<String, Property> properties = parseEntityProperties(obj.get("properties").getAsJsonObject());
		entity.setProperties(properties);
		return entity;
	}

	private static Map<String, Property> parseEntityProperties(JsonObject obj) {
		Set<Entry<String, JsonElement>> set = obj.entrySet();
		for (Entry<String, JsonElement> entry : set) {
			String name = entry.getKey();
			JsonObject value = entry.getValue().getAsJsonObject();
			Property property = new Property();
			// property.setValue(parseValue(value));
		}
		return null;
	}

	public static String parseId(JsonArray array) {
		List<Object> ret = new ArrayList<Object>(array.size() * 2);
		for (JsonElement element : array) {
			JsonObject obj = element.getAsJsonObject();
			ret.add(obj.get("kind").getAsString());
			if (obj.has("name")) {
				ret.add(obj.get("name").getAsString());
			} else {
				throw new RuntimeException("wrong: " + obj);
			}
		}
		return FlexJson.toJSON(ret);
	}
}
