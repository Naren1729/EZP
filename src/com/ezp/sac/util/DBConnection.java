package com.ezp.sac.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	static Connection connection;
	
	// JDBC connection details
    private static final String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Replace with your DB URL
    private static final String username = "system"; // Replace with your DB username
    private static final String password = "Natwest123"; // Replace with your DB password

    // Private constructor to prevent instantiation
    private DBConnection() {}

    // Static method to get the database connection
    public static Connection getConnection() throws SQLException {
    	try {
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		connection = DriverManager.getConnection(url, username, password);
    		System.out.println("Connection Successful!!");
    	}catch(SQLException | ClassNotFoundException e) {
    		System.out.println(e.getMessage());
    		
    	}
    	
        return connection;
    }

}
