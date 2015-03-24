package com.murerz.dsopz.ds.exp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.murerz.dsopz.ds.query.FullQuery;
import com.murerz.dsopz.util.Util;

public class Exporter {

	private String driver;

	private String url;

	private String user;

	private String pass;

	private FullQuery query;

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setQuery(FullQuery query) {
		this.query = query;
	}

	public void export() {
		Connection conn = openConnection();
		try {
			
		} finally {
			Util.close(conn);
		}
	}

	private Connection openConnection() {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Exporter exporter = new Exporter();
		exporter.setDriver("org.hsqldb.jdbcDriver");
		exporter.setUrl("jdbc:hsqldb:mem:.");
		exporter.setUser("sa");

		FullQuery query = new FullQuery();
		query.setDataset("quero-natura").setNamespace("staging");
		query.setPageSize(30l);
		exporter.setQuery(query);
		exporter.export();
	}

}
