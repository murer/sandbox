package com.murerz.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBTest {

	private String driver;
	private String url;
	private String user;
	private String pass;
	private String query;
	private long times;

	public String getDriver() {
		return driver;
	}

	public DBTest setDriver(String driver) {
		try {
			this.driver = driver;
			Class.forName(driver);
			return this;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public String getUrl() {
		return url;
	}

	public DBTest setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUser() {
		return user;
	}

	public DBTest setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPass() {
		return pass;
	}

	public DBTest setPass(String pass) {
		this.pass = pass;
		return this;
	}

	public String getQuery() {
		return query;
	}

	public DBTest setQuery(String query) {
		this.query = query;
		return this;
	}

	public long getTimes() {
		return times;
	}

	public DBTest setTimes(long times) {
		this.times = times;
		return this;
	}

	public static void main(String[] args) {
		String driver = System.getProperty("dbtest.driver", "org.hsqldb.jdbcDriver");
		String url = System.getProperty("dbtest.url", "jdbc:hsqldb:mem:.");
		String user = System.getProperty("dbtest.user", "sa");
		String pass = System.getProperty("dbtest.pass", "");
		String query = System.getProperty("dbtest.query", "select 1 from (VALUES(0))");
		long times = Long.parseLong(System.getProperty("dbtest.time", "10000"));

		DBTest test = new DBTest();
		test.setDriver(driver).setUrl(url).setUser(user).setPass(pass).setQuery(query).setTimes(times);

		test.test();

	}

	private void test() {
		for (long i = 0; i < times; i++) {
			System.out.println("Try: " + i);
			check();
		}
	}

	private void check() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(url, user, pass);
			ps = conn.prepareStatement(query);
			ResultSet query = ps.executeQuery();
			while(query.next()) {
				query.getString(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
	}

	private void close(AutoCloseable o) {
		if (o != null) {
			try {
				o.close();
			} catch (Exception e) {
				System.out.println("error closing");
				e.printStackTrace(System.out);
			}
		}
	}

}
