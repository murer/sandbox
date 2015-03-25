package com.murerz.dsopz.ds.main;

import java.util.Iterator;

import com.google.gson.JsonObject;
import com.murerz.dsopz.ds.query.FullQuery;
import com.murerz.dsopz.util.Console;

public class Exporter {

	public static void main(String[] args) {
		FullQuery query = new FullQuery();
		query.setDataset(args[0]);
		query.setNamespace(args[1]);
		query.setPageSize(1000l);
		Iterator<JsonObject> it = query.query();
		long count = 0;
		while (it.hasNext()) {
			count++;
			if (count % 1000 == 0) {
				Console.me().errPrintln("Exported: " + count);
			}
			Console.me().println(it.next());
		}

	}

}
