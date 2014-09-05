package com.operativa.simulator.datamodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.operativa.properties.Parameters;
import com.operativa.properties.PropertyManager;

public class DBManager {

	private static DBManager dbManager = null;
	private Connection connection;

	private DBManager() {
		try {
			PropertyManager pManager = PropertyManager.instance();
			Class.forName(pManager.getProperty(Parameters.DB_DRIVER.toString()));
			try {
				connection = DriverManager.getConnection(pManager
						.getProperty(Parameters.DB_NAME.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public static DBManager instance() {

		if (dbManager == null) {
			dbManager = new DBManager();
		}

		return dbManager;
	}

	public ResultSet executeQuery(String query) {
		Statement statement;
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void executeUpdate(String updateStatement) {
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(updateStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insert(String insertStatement) {
		Statement statement;
		try {
			statement = connection.createStatement();
			System.out.println(insertStatement);
			statement.execute(insertStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finalize() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
