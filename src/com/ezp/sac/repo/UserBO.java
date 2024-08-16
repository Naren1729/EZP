/**
 * @Authors : Bhavansh, Arvind, Keerthana
 * @Date : 11/08/2024
 * 
 * @Description:
 * The UserBO class is a singleton responsible for managing user data within the system. 
 * It provides methods to initialize dummy users, retrieve users by username, update user 
 * details, and return the list of all users. It also integrates with the FraudDetectionService 
 * to flag suspicious activities when a username is not found.
 */

package com.ezp.sac.repo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ezp.sac.model.User;
import com.ezp.sac.util.DBConnection;

public class UserBO {
    private List<User> users;
    private boolean initialized = false;

    // Singleton instance
    private static UserBO instance;    
    
    //JDBC Connection
    private Connection connection;

    // Private constructor to prevent instantiation
    private UserBO() {
    	try {
            connection = DBConnection.getConnection();
            initializeDummyUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Public method to provide access to the singleton instance
    public static synchronized UserBO getInstance() {
        if (instance == null) {
            instance = new UserBO();
        }
        return instance;
    } 
    
    // Method to initialize a list of dummy users with predefined data
//    private void initializeDummyUsers() {
//        if (!initialized) { // Check if initialization is needed
//            System.out.println("Array List Created!!!");
//            users = new ArrayList<>();
//            users.add(new User("johnDoe", "John Doe", "password123", 1001L, "deposit", 250.75, LocalDateTime.now(), "completed"));
//            users.add(new User("janeSmith", "Jane Smith", "password456", 1002L, "withdrawal", 100.50, LocalDateTime.now(), "pending"));
//            users.add(new User("aliceJones", "Alice Jones", "password789", 1003L, "deposit", 500.00, LocalDateTime.now(), "completed"));
//            users.add(new User("bobBrown", "Bob Brown", "password012", 1004L, "transfer", 150.25, LocalDateTime.now(), "failed"));
//            users.add(new User("carolWhite", "Carol White", "password345", 1005L, "deposit", 75.00, LocalDateTime.now(), "completed"));
//            users.add(new User("keerthanaB", "Keerthana B", "password123", 1001L, "deposit", 250.75, LocalDateTime.now(), "completed"));
//            users.add(new User("bhavanshG", "Bhavansh G", "password456", 1002L, "withdrawal", 100.50, LocalDateTime.now(), "pending"));
//            users.add(new User("alisonRose", "Alison Rose", "password789", 1003L, "deposit", 500.00, LocalDateTime.now(), "completed"));
//            initialized = true; // Set flag to true after initialization
//        }
//    }
    
    
    // Method to initialize a list of dummy users with predefined data
    private void initializeDummyUsers() {
        if (!initialized) { // Check if initialization is needed
            System.out.println("Initializing users from database...");
            users = new ArrayList<>();
            String query = "SELECT * FROM users";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getLong("transaction_id"),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("transaction_time").toLocalDateTime(),
                            rs.getString("status")
                    );
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initialized = true; // Set flag to true after initialization
        }
    }
    

 // Method to get a user by their username
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getLong("transaction_id"),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("transaction_time").toLocalDateTime(),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to return the list of all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users); // Return a copy of the list to prevent modification
    }

 // Method to update user details
    public void updateUser(User updatedUser) {
        String query = "UPDATE users SET name = ?, password = ?, transaction_id = ?, transaction_type = ?, amount = ?, transaction_time = ?, status = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, updatedUser.getName());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setLong(3, updatedUser.getTransaction_id());
            pstmt.setString(4, updatedUser.getType());
            pstmt.setDouble(5, updatedUser.getAmount());
            pstmt.setTimestamp(6, Timestamp.valueOf(updatedUser.getDate()));
            pstmt.setString(7, updatedUser.getStatus());
            pstmt.setString(8, updatedUser.getUsername());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update the in-memory list
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                        users.set(i, updatedUser);
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
