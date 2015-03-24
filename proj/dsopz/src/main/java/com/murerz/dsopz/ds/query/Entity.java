package com.murerz.dsopz.ds.query;

import java.util.HashMap;
import java.util.Map;

import flexjson.JSON;

public class Entity {

	private String id;

	private Map<String, Property> properties = new HashMap<String, Property>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JSON(include = true)
	public Map<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "[Entity " + id + " " + properties + "]";
	}

}
