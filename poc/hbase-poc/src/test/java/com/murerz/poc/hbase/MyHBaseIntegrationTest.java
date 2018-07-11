package com.murerz.poc.hbase;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHBaseIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(MyHBaseIntegrationTest.class);

	private static HBaseTestingUtility utility;
	byte[] CF = "CF".getBytes();
	byte[] CQ1 = "CQ-1".getBytes();
	byte[] CQ2 = "CQ-2".getBytes();

	@Before
	public void setUp() throws Exception {
		if (utility == null) {
			utility = new HBaseTestingUtility();
			utility.startMiniCluster();
		}
	}

	@After
	public void tearDown() {
		if (utility != null) {
			try {
				utility.shutdownMiniCluster();
				// utility.deleteTable("MyTest");
			} catch (Exception e) {
				LOG.error("error closing", e);
			}
		}
		utility = null;
	}

	@Test
	public void testInsert() throws Exception {
		Table table = utility.createTable(Bytes.toBytes("MyTest"), CF);
		HBaseTestObj obj = new HBaseTestObj();
		obj.setRowKey("ROWKEY-1");
		obj.setData1("DATA-1");
		obj.setData2("DATA-2");
		MyHBaseDAO.insertRecord(table, obj);
		Get get1 = new Get(Bytes.toBytes(obj.getRowKey()));
		get1.addColumn(CF, CQ1);
		Result result1 = table.get(get1);
		assertEquals(Bytes.toString(result1.getRow()), obj.getRowKey());
		assertEquals(Bytes.toString(result1.value()), obj.getData1());
		Get get2 = new Get(Bytes.toBytes(obj.getRowKey()));
		get2.addColumn(CF, CQ2);
		Result result2 = table.get(get2);
		assertEquals(Bytes.toString(result2.getRow()), obj.getRowKey());
		assertEquals(Bytes.toString(result2.value()), obj.getData2());
	}

	@Test
	public void testInsertTwice() throws Exception {
		testInsert();
	}
}