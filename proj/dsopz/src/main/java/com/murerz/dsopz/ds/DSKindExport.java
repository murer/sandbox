package com.murerz.dsopz.ds;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.murerz.dsopz.oauth.OAuth;
import com.murerz.dsopz.util.HttpUtil;
import com.murerz.dsopz.util.Util;

public class DSKindExport {

	private String dataset;

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	private void export() {
		InputStream in = null;
		try {
			Request req = Request.Post("https://www.googleapis.com/datastore/v1beta2/datasets/quero-natura/runQuery");
			OAuth.me().config(req);
			req.bodyString("{\"partitionId\":{\"namespace\":\"staging\"},\"query\":{}}", ContentType.APPLICATION_JSON);
			HttpResponse resp = req.execute().returnResponse();
			HttpUtil.checkError(resp);
			in = resp.getEntity().getContent();
			Util.copyAll(in, new File("/tmp/abc"));
			System.out.println(resp.getEntity().getContentLength());
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			Util.close(in);
		}
	}

	public static void main(String[] args) {
		DSKindExport export = new DSKindExport();
		export.setDataset("quero-natura");
		export.export();
	}

}
