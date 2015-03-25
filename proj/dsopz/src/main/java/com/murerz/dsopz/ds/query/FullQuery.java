package com.murerz.dsopz.ds.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gson.JsonObject;

public class FullQuery {

	private PageQuery query;

	private int index = 0;

	public FullQuery() {
		this.query = new PageQuery();
	}

	public FullQuery setPageSize(Long pageSize) {
		this.query.setLimit(pageSize);
		return this;
	}

	public FullQuery setNamespace(String namespace) {
		this.query.setNamespace(namespace);
		return this;
	}

	public FullQuery setDataset(String dataset) {
		this.query.setDataset(dataset);
		return this;
	}

	public Iterator<JsonObject> query() {
		query.query();
		return new Iterator<JsonObject>() {

			@Override
			public boolean hasNext() {
				return (index < query.getEntities().size());
			}

			@Override
			public JsonObject next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				JsonObject ret = query.getEntities().get(index++).getAsJsonObject();
				if (!hasNext() && query.hasMoreElements()) {
					index = 0;
					query.nextPage();
					query.query();
				}
				return ret;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public static void main(String[] args) {
		FullQuery query = new FullQuery();
		query.setDataset("quero-natura").setNamespace("staging");
		query.setPageSize(10l);
		Iterator<JsonObject> entities = query.query();
		int i = 0;
		while (entities.hasNext()) {
			System.out.println(entities.next());
			if (i++ > 30) {
				break;
			}
		}
	}

}
