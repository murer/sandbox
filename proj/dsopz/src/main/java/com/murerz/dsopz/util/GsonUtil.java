package com.murerz.dsopz.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class GsonUtil {

	public static JsonElement parse(String json) {
		return new JsonParser().parse(json);
	}

	public static JsonObject getAsJsonObject(Object obj) {
		return new Gson().toJsonTree(obj).getAsJsonObject();
	}

	public static JsonObject getAsJsonObject(Object obj, String... atributos) {
		JsonObject jsonObj = new JsonObject();

		for (String atributo : atributos) {
			Object valorDoAtributo = ReflectionUtil.getBeanField(obj, atributo);
			JsonPrimitive getAsJsonPrimitive = getAsJsonPrimitive(valorDoAtributo);
			jsonObj.add(atributo, getAsJsonPrimitive);
		}

		return jsonObj;
	}

	public static JsonPrimitive getAsJsonPrimitive(Object valorDoAtributo) {
		return new Gson().toJsonTree(valorDoAtributo).getAsJsonPrimitive();
	}

	public static JsonPrimitive toJsonPrimitive(Object valor) {
		if (valor instanceof Boolean) {
			return new JsonPrimitive((Boolean) valor);
		} else if (valor instanceof String) {
			return new JsonPrimitive((String) valor);
		} else if (valor instanceof Number) {
			return new JsonPrimitive((Number) valor);
		} else if (valor instanceof Character) {
			return new JsonPrimitive((Character) valor);
		} else if (valor == null) {
			return new JsonPrimitive("");
		}
		throw new RuntimeException("wrong: " + valor);
	}

	public static JsonArray getAsJsonArray(Object obj) {
		return new Gson().toJsonTree(obj).getAsJsonArray();
	}

	public static List<JsonObject> getAsList(List<?> objs) {
		List<JsonObject> result = new ArrayList<JsonObject>();

		for (Object obj : objs) {
			result.add(getAsJsonObject(obj));
		}

		return result;
	}

	public static List<Long> jsonArrayToListOfLong(JsonArray jsonArray) {
		List<Long> list = new ArrayList<Long>();
		for (JsonElement json : jsonArray) {
			list.add(json.getAsLong());
		}
		return list;
	}

	public static JsonObject createObject(Object... values) {
		if (values.length % 2 != 0) {
			throw new RuntimeException("invalid length");
		}
		JsonObject ret = new JsonObject();
		for (int i = 0; i < values.length; i += 2) {
			String name = (String) values[i];
			Object value = values[i + 1];
			if (value == null) {
				value = JsonNull.INSTANCE;
			}
			if (!(value instanceof JsonElement)) {
				value = parse(value);
			}
			ret.add(name, (JsonElement) value);
		}
		return ret;

	}

	@SuppressWarnings("unchecked")
	public static JsonElement createBasic(Object value) {
		if (value == null) {
			return JsonNull.INSTANCE;
		}
		if (value instanceof Boolean) {
			return new JsonPrimitive(((Boolean) value));
		}
		if (value instanceof Number) {
			return new JsonPrimitive((Number) value);
		}
		if (value instanceof String) {
			return new JsonPrimitive((String) value);
		}
		if (value instanceof Iterable) {
			Iterable<Object> it = (Iterable<Object>) value;
			JsonArray ret = new JsonArray();
			for (Object object : it) {
				ret.add(createBasic(object));
			}
			return ret;
		}
		throw new UnsupportedOperationException("unsupported type: " + value);
	}

	public static JsonObject extend(JsonObject ret, JsonObject... values) {
		if (ret == null || ret.isJsonNull()) {
			ret = new JsonObject();
		}
		for (JsonObject obj : values) {
			Set<Entry<String, JsonElement>> set = obj.entrySet();
			for (Entry<String, JsonElement> entry : set) {
				ret.add(entry.getKey(), entry.getValue());
			}
		}
		return ret;
	}

	public static JsonElement createJsonArray(Object... values) {
		JsonArray ret = new JsonArray();
		Gson gson = new Gson();
		for (int i = 0; i < values.length; i++) {
			Object value = values[i];
			if (value == null) {
				value = JsonNull.INSTANCE;
			}
			if (!(value instanceof JsonElement)) {
				value = gson.toJsonTree(value);
			}
			ret.add((JsonElement) value);
		}
		return ret;
	}

	public static Object v(JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return null;
		}
		if (element.isJsonPrimitive()) {
			if (element.getAsJsonPrimitive().isNumber()) {
				return element.getAsLong();
			}
			if (element.getAsJsonPrimitive().isString()) {
				return element.getAsString();
			}
			if (element.getAsJsonPrimitive().isBoolean()) {
				return element.getAsBoolean();
			}
		}

		throw new RuntimeException("unsupported: " + element);
	}

	public static JsonObject extend(JsonObject jsonObject, Object... values) {
		if (values.length % 2 != 0) {
			throw new RuntimeException("invalid length");
		}
		if (jsonObject == null) {
			jsonObject = new JsonObject();
		}
		for (int i = 0; i < values.length; i += 2) {
			String name = (String) values[i];
			Object value = values[i + 1];

			if (value instanceof String) {
				jsonObject.addProperty(name, (String) value);
			} else if (value instanceof Number) {
				jsonObject.addProperty(name, (Number) value);
			} else if (value instanceof JsonElement) {
				jsonObject.add(name, (JsonElement) value);
			} else if (value instanceof Character) {
				jsonObject.addProperty(name, (Character) value);
			} else if (value instanceof Boolean) {
				jsonObject.addProperty(name, (Boolean) value);
			}

		}
		return jsonObject;
	}

	public static JsonArray getAsJsonArray(List<Object[]> objetos, String... keys) {
		JsonArray jsonArray = new JsonArray();

		for (Object[] object : objetos) {
			JsonObject jsonObject = new JsonObject();
			for (int i = 0; i < keys.length; i++) {
				jsonObject = GsonUtil.extend(jsonObject, keys[i], object[i]);
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public static String getAsString(JsonObject obj, String key) {
		if (obj != null) {
			JsonElement value = obj.get(key);
			if (value != null) {
				if(value.isJsonNull()){
					return null;
				}
				
				return value.getAsString();
			}
		}
		return null;
	}

	public static Long getAsLong(JsonObject obj, String key) {
		if (obj != null) {
			JsonElement value = obj.get(key);
			if (value != null) {
				if(value.isJsonNull()){
					return null;
				}
				
				return value.getAsLong();
			}
		}
		return null;
	}

	public static BigDecimal getAsBigDecimal(JsonObject obj, String key) {
		if (obj != null) {
			JsonElement value = obj.get(key);
			if (value != null) {
				if(value.isJsonNull()){
					return null;
				}
				
				return value.getAsBigDecimal();
			}
		}
		return null;
	}
	
	public static JsonElement parse(Reader in) {
		return new JsonParser().parse(in);
	}

	public static JsonElement parse(InputStream in, String charset) {
		try {
			return parse(new InputStreamReader(in, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonElement parse(byte[] content, String charset) {
		return parse(new ByteArrayInputStream(content), charset);
	}

	public static String pretty(JsonElement json) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(json);
	}

	public static JsonElement parse(Object obj) {
		return new Gson().toJsonTree(obj);
	}

}
