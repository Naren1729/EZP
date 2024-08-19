/**
 * @Authors : Keerthana B
 * @Date : 19/08/2024
 * 
 * @Description:
 * The UserBO class is responsible for managing user data in the database. 
 * It provides functionality to insert new records, verify the existence 
 * of a username, display all table contents, and update user records with 
 * encrypted or decrypted details. This class also initializes the database 
 * with predefined dummy users, ensuring consistent data for operations.
 */

package com.ezp.sac.repo;

import com.ezp.sac.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import com.ezp.sac.util.DbConnection;

public class UserBO {
    private boolean initialized = false;

    // Singleton instance
    private static UserBO instance;    
    
    // Private constructor to prevent instantiation
    private UserBO() {
    	initializeDatabaseTable();
    }

    // Public method to provide access to the singleton instance
    public static synchronized UserBO getInstance() {
        if (instance == null) {
            instance = new UserBO();
        }
        return instance;
    } 
    
  public List<User> getAllUsers(){
	List<User> users = new ArrayList<>();
	try(Connection conn = DbConnection.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from users"); //SELECT all users from table
			ResultSet rs = pst.executeQuery()){
		while(rs.next()) { //get result set from the query 
			String username = rs.getString("username");
            String name = rs.getString("name");
            String password = rs.getString("password");
            long transactionId = rs.getLong("transaction_id");
            String transactionType = rs.getString("transaction_type");
            double amount = rs.getDouble("amount");
            LocalDateTime transactionTime = rs.getTimestamp("transaction_time").toLocalDateTime();
            String status = rs.getString("status");
            
            User user = new User(username,name,password,transactionId,transactionType,amount,transactionTime,status); 
            users.add(user); //adding existing user to the new arrayList
		}
	}catch (SQLException e) {
        e.printStackTrace();
    }
	return users;
}
    
	// Initial connection using JDBC
	private void initializeDatabaseTable() {
		try (Connection connection = DbConnection.getConnection()) { 
		    if (!initialized) {
		        System.out.println("Created table in given database...");

		        // No need to delete records, as the table is already created

		        connection.commit(); // Committing changes made
		        initialized = true;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		    
		
		return;

	}

	// Method to get a user by their username and check if the entered username
	// exists or not
	public User getUserByUsername(String username) {


		try (Connection connection = DbConnection.getConnection();
				PreparedStatement preparedStatementSelect = connection
						.prepareStatement("SELECT * FROM users WHERE username = \'" + username + "\'");  // Select if any record's username matches given username
				ResultSet resultSet = preparedStatementSelect.executeQuery();) { // Storing the result table into resultSet

			if(!resultSet.next())
				return null;
			String obtainedUsername = resultSet.getString(1);
			if (obtainedUsername != null) {
				connection.commit();
				// Converting and passing the matched record as a class reference
				return new User(obtainedUsername, resultSet.getString(2), resultSet.getString(3), resultSet.getLong(4),
						resultSet.getString(5), resultSet.getDouble(6), (resultSet.getTimestamp(7)).toLocalDateTime(),
						resultSet.getString(8));
				// Converting Timestamp type of table attribute to LocalDateTime type of class
				// attribute
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Return null if the user is not found

	}

	// Method to update user details
	public void updateUser(User updatedUser, String username) {
		try (Connection connection = DbConnection.getConnection();
				PreparedStatement preparedStatementUpdate = connection.prepareStatement(
						"UPDATE users SET username = ?, name = ?, password = ?, transaction_id = ?, transaction_type = ?, amount = ?, transaction_time = ?, status = ? WHERE username = ?");) {
			// Updating the table record whose username matches the given username with the corresponding encrypted / decrypted records
			preparedStatementUpdate.setString(1, updatedUser.getUsername());
			preparedStatementUpdate.setString(2, updatedUser.getName());
			preparedStatementUpdate.setString(3, updatedUser.getPassword());
			preparedStatementUpdate.setLong(4, updatedUser.getTransaction_id());
			preparedStatementUpdate.setString(5, updatedUser.getType());
			preparedStatementUpdate.setDouble(6, updatedUser.getAmount());
			preparedStatementUpdate.setTimestamp(7, Timestamp.valueOf(updatedUser.getDate()));
			preparedStatementUpdate.setString(8, updatedUser.getStatus());
			preparedStatementUpdate.setString(9, username);
			preparedStatementUpdate.executeUpdate();
			//connection.commit();
			return;
			// Handle case where user does not exist, e.g., throw an exception or log an
			// error
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	// Method to Display the table of all users
	public void displayUserTable(){
		try (Connection connection = DbConnection.getConnection();
				PreparedStatement preparedStatementSelectAll = connection.prepareStatement("SELECT * FROM users");
				ResultSet resultSet = preparedStatementSelectAll.executeQuery())
		{
		
		connection.commit();
		// Get metadata to dynamically handle column names and count
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();

		// Print column names
		for (int i = 1; i <= columnCount; i++) {
			System.out.printf("%-20s", metaData.getColumnName(i)); // data length adjusted
		}
		System.out.println("\n" + "-".repeat(20 * columnCount)); // Separator line

		// Print each record
		while (resultSet.next()) {
			for (int i = 1; i <= columnCount; i++) {
				String columnValue = resultSet.getString(i);
				System.out.printf("%-20s", columnValue);
			}
			System.out.println();
		}
	}catch (SQLException e) {
		e.printStackTrace();
	}
		return;
		
	}
    
}
    


    
	
