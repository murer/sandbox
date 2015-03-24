package com.murerz.dsopz.ds.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.murerz.dsopz.util.FlexJson;

public class FullQuery {

	private PageQuery query;

	private int index = 0;

	public FullQuery() {
		this.query = new PageQuery();
	}

	private FullQuery setPageSize(Long pageSize) {
		this.query.setLimit(pageSize);
		return this;
	}

	private FullQuery setNamespace(String namespace) {
		this.query.setNamespace(namespace);
		return this;
	}

	private FullQuery setDataset(String dataset) {
		this.query.setDataset(dataset);
		return this;
	}

	private Iterator<Entity> query() {
		query.query();
		return new Iterator<Entity>() {

			@Override
			public boolean hasNext() {
				return (index < query.getEntities().size());
			}

			@Override
			public Entity next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				Entity ret = query.getEntities().get(index++);
				if (!hasNext() && query.hasMoreElements()) {
					index = 0;
					query.nextPage();
					query.query();
				}
				return ret;
			}

		};
	}

	public static void main(String[] args) {
		FullQuery query = new FullQuery();
		query.setDataset("quero-natura").setNamespace("staging");
		query.setPageSize(10l);
		Iterator<Entity> entities = query.query();
		int i = 0;
		while (entities.hasNext()) {
			System.out.println(FlexJson.toJSON(entities.next()));
			if (i++ > 30) {
				break;
			}
		}
	}

}
