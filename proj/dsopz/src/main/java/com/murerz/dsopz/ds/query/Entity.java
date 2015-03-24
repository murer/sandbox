package com.murerz.dsopz.ds.query;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	private String id;

	private Map<String, Property> properties = new HashMap<String, Property>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

}
