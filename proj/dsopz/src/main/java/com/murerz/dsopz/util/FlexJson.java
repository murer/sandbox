package com.murerz.dsopz.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import flexjson.JSONDeserializer;
import flexjson.JSONException;
import flexjson.JSONSerializer;

public class FlexJson {

	private static FlexJson ME = null;

	protected JSONSerializer serializer;

	private FlexJson() {

	}

	public static FlexJson instance() {
		if (ME == null) {
			synchronized (FlexJson.class) {
				if (ME == null) {
					ME = create();
				}
			}
		}
		return ME;
	}

	private static FlexJson create() {
		FlexJson ret = new FlexJson();
		ret.serializer = new JSONSerializer();
		return ret;
	}

	public <T> T parse(InputStream in, String charset, Class<T> type) {
		try {
			return parse(new InputStreamReader(in, charset), type);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T parse(Reader in, Class<T> type) {
		JSONDeserializer<T> d = createDeserializer();
		T ret;
		try {
			ret = d.deserialize(in, type);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	public <T> T parse(String json, Class<T> type) {
		JSONDeserializer<T> d = createDeserializer();
		T ret;
		try {
			ret = d.deserialize(json, type);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return ret;
	}

	private <T> JSONDeserializer<T> createDeserializer() {
		JSONDeserializer<T> ret = new JSONDeserializer<T>();
		return ret;
	}

	public String format(Object obj) {
		String ret;
		try {
			ret = serializer.serialize(obj);
		} catch (JSONException e) {
			throw new RuntimeException("Object: " + obj + "\n" + e);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> parseList(String json, Class<T> clazz) {
		JSONDeserializer<T> d = createDeserializer();
		return (List<T>) d.use("values", clazz).deserialize(json);
	}

	public Object parse(String json) {
		JSONDeserializer<Object> d = createDeserializer();
		return d.deserialize(json);
	}

	public <T> T parse(byte[] data, String charset, Class<T> type) {
		return parse(new ByteArrayInputStream(data), charset, type);
	}

	public static String pretty(Object obj) {
		return new JSONSerializer().prettyPrint(true).serialize(obj);
	}

	public static String prettySimple(Object obj) {
		return new JSONSerializer().exclude("*.class").prettyPrint(true).serialize(obj);
	}

	public static String toJSON(Object obj) {
		return new JSONSerializer().exclude("*.class").serialize(obj);
	}

	public static String toJSONClass(Object obj) {
		return instance().format(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return instance().parse(json, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> fromGZipedListJson(byte[] data) {
		String gunzip = Util.gunzip(data, "UTF-8");
		return (List<T>) instance().parseList(gunzip, String.class);
	}

	public static byte[] toGZipedListJson(List<String> deliveredCN) {
		String json = toJSON(deliveredCN);
		return Util.gzip(json, "UTF-8");
	}

	public static <T> T parseFile(File file, Class<T> clazz) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			String str = Util.read(file.toURI().toURL(), "UTF-8");
			return FlexJson.instance().parse(str, clazz);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeFile(File file, Object data) {
		File parent = file.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		String str = prettySimple(data);
		Util.write(file, str);
	}

}
