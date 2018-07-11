package com.murerz.poc.hbase;

public class HBaseTestObj {

	private String data1;
	private String rowKey;
	private String data2;

	public String getData1() {
		return data1;
	}

	public HBaseTestObj setData1(String data1) {
		this.data1 = data1;
		return this;
	}

	public String getRowKey() {
		return rowKey;
	}

	public HBaseTestObj setRowKey(String rowKey) {
		this.rowKey = rowKey;
		return this;
	}

	public String getData2() {
		return data2;
	}

	public HBaseTestObj setData2(String data2) {
		this.data2 = data2;
		return this;
	}

}
