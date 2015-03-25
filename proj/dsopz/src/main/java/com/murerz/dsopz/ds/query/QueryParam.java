package com.murerz.dsopz.ds.query;

import java.util.ArrayList;
import java.util.List;

import com.murerz.dsopz.util.FlexJson;

import flexjson.JSON;

public class QueryParam {

	public static class Property {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class Projection {
		private Property property;

		public Property getProperty() {
			return property;
		}

		public void setProperty(Property property) {
			this.property = property;
		}

	}

	public static class Query {
		private List<Property> kinds;
		private Long limit = 10l;
		private String startCursor;
		private List<Projection> projection;

		@JSON(include = true)
		public List<Projection> getProjection() {
			return projection;
		}

		public void setProjection(List<Projection> projection) {
			this.projection = projection;
		}

		@JSON(include = true)
		public List<Property> getKinds() {
			return kinds;
		}

		public void setKinds(List<Property> kinds) {
			this.kinds = kinds;
		}

		public Long getLimit() {
			return limit;
		}

		public void setLimit(Long limit) {
			this.limit = limit;
		}

		public String getStartCursor() {
			return startCursor;
		}

		public void setStartCursor(String startCursor) {
			this.startCursor = startCursor;
		}

	}

	public static class PartitionId {
		private String namespace;

		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

	}

	private Query query = new Query();

	private PartitionId partitionId = new PartitionId();

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public PartitionId getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(PartitionId partitionId) {
		this.partitionId = partitionId;
	}

	public QueryParam namespace(String namespace) {
		partitionId.setNamespace(namespace);
		return this;
	}

	public QueryParam kind(String kind) {
		Property k = new Property();
		k.setName(kind);
		List<Property> l = new ArrayList<QueryParam.Property>();
		l.add(k);
		query.setKinds(l);
		return this;
	}

	public QueryParam limit(long limit) {
		query.setLimit(limit);
		return this;
	}

	public QueryParam setStartCursor(String startCursor) {
		query.setStartCursor(startCursor);
		return this;
	}

	public QueryParam keysOnly(boolean keysOnly) {
		if (!keysOnly) {
			query.setProjection(null);
			return this;
		}
		List<Projection> l = new ArrayList<Projection>();
		Projection p = new Projection();
		Property prop = new Property();
		prop.setName("__key__");
		p.setProperty(prop);
		l.add(p);
		query.setProjection(l);
		return this;
	}

	public static void main(String[] args) {
		QueryParam param = new QueryParam();
		param.kind("__kind__");
		param.namespace("poc");
		param.limit(10l);
		param.keysOnly(true);
		System.out.println(FlexJson.toJSON(param));
	}

}
