package com.murerz.httpcachez;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public class Job {

	private static Map<Key, Job> jobs = new HashMap<Key, Job>();

	private Key key;

	public static synchronized Job get(Key key) {
		Job job = jobs.get(key);
		if (job == null) {
			job = new Job();
			job.key = key;
			jobs.put(key, job);
		}
		return job;
	}

	public void execute(HttpRequest req, HttpResponse resp) {
		if (cache(req, resp)) {
			return;
		}
		proxy(req, resp);
	}

	private void proxy(HttpRequest req, HttpResponse resp) {
		String url = key.toURL();
		HttpResponse response = HTTP.me().get(url);
		resp.setStatusLine(response.getStatusLine());
		resp.setHeader(response.getFirstHeader("Content-Type"));
		resp.setEntity(response.getEntity());
	}

	private synchronized boolean cache(HttpRequest req, HttpResponse resp) {
		return false;
	}

}
