package com.witgather.utils;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

/**
 * @author 陈康勇
 *	对DBCP进行封装
 */
public class DatabasePool {
	private static Properties properties = new Properties();
	private static DataSource dataSource;
	static {
		try {
			FileInputStream fis = new FileInputStream("resources\\db.properties");
			properties.load(fis);
			dataSource = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() {
		Connection con = null;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
