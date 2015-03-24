package com.murerz.dsopz.ds.query;

import java.util.Iterator;
import java.util.NoSuchElementException;

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

	public Iterator<Entity> query() {
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
			System.out.println(entities.next());
			if (i++ > 30) {
				break;
			}
		}
	}

}
