package com.murerz.dsopz.ds.query;

import java.util.List;

public class KindQuery {

	private String dataset;

	private Long limit = 1000l;

	public List<Entity> query() {
		return null;
	}
	
	public static void main(String[] args) {
		KindQuery query = new KindQuery();
		List<Entity> list = query.query();
		System.out.println(list);
	}

}
