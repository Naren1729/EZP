/**
 * @Author: Keerthana B, Mayuri
 * @Date : 19/08/2024
 * 
 * @Description:
 * The DbConnection class is responsible for establishing a connection 
 * to the Oracle database. It utilizes JDBC to connect to the database 
 * using a provided URL, username, and password. This class ensures 
 * a single connection instance and handles any SQL or class loading 
 * exceptions that may arise during the connection process. The connection 
 * is established with properties to securely store user credentials.
 */
package com.ezp.sac.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

	static Connection connection;
	static int flag = 1;

	// Private constructor to prevent instantiation
	private DbConnection() {
	}

	// Static method to get the database connection
	public static Connection getConnection() throws SQLException {
		try {
			// JDBC connection details
			final String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Replace with your DB URL
			final String username = "system"; // Replace with your DB username
			final String password = "Natwest123"; // Replace with your DB password
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Creating object of Property class
			Properties prop = new Properties();
			// Storing the properties of USERS Database (USERNAME & PASSWORD) into the
			// Property object
			// This maps the key to the specified value in this hashtable.
			prop.put("user", username);
			prop.put("password", password);
			connection = DriverManager.getConnection(url, prop);
			if (flag == 1) {

				System.out.println("Connection Successful!!"); // Print only for first time connection
				flag = 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());

		}

		return connection;
	}

}