package com.murerz.httpcachez;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public class Job {

	private static Map<Key, Job> jobs = new HashMap<Key, Job>();

	public static synchronized Job get(Key key) {
		Job job = jobs.get(key);
		if (job == null) {
			job = new Job();
			jobs.put(key, job);
		}
		return job;
	}

	public void execute(HttpRequest req, HttpResponse resp) {
		
	}

}
