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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ezp.sac.model.User;
import com.ezp.sac.service.FraudDetectionService;

public class UserBO {
    private List<User> users;
    private boolean initialized = false;
    private int flag = 0;

    // Singleton instance
    private static UserBO instance;

    // Reference to FraudDetectionService
    private FraudDetectionService fraudDetectionService;

    // Private constructor to prevent instantiation
    private UserBO() {
        initializeDummyUsers();
    }

    // Public method to provide access to the singleton instance
    public static synchronized UserBO getInstance() {
        if (instance == null) {
            instance = new UserBO();
        }
        return instance;
    }

    // Method to set FraudDetectionService after UserBO is created
    public void setFraudDetectionService(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    // Method to initialize a list of dummy users with predefined data
    private void initializeDummyUsers() {
        if (!initialized) { // Check if initialization is needed
            System.out.println("Array List Created!!!");
            users = new ArrayList<>();
            users.add(new User("johnDoe", "John Doe", "password123", 1001L, "deposit", 250.75, LocalDateTime.now(), "completed"));
            users.add(new User("janeSmith", "Jane Smith", "password456", 1002L, "withdrawal", 100.50, LocalDateTime.now(), "pending"));
            users.add(new User("aliceJones", "Alice Jones", "password789", 1003L, "deposit", 500.00, LocalDateTime.now(), "completed"));
            users.add(new User("bobBrown", "Bob Brown", "password012", 1004L, "transfer", 150.25, LocalDateTime.now(), "failed"));
            users.add(new User("carolWhite", "Carol White", "password345", 1005L, "deposit", 75.00, LocalDateTime.now(), "completed"));
            users.add(new User("keerthanaB", "Keerthana B", "password123", 1001L, "deposit", 250.75, LocalDateTime.now(), "completed"));
            users.add(new User("bhavanshG", "Bhavansh G", "password456", 1002L, "withdrawal", 100.50, LocalDateTime.now(), "pending"));
            users.add(new User("alisonRose", "Alison Rose", "password789", 1003L, "deposit", 500.00, LocalDateTime.now(), "completed"));
            initialized = true; // Set flag to true after initialization
        }
    }

    // Method to get a user by their username
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                flag = 1;
                return user;
            }
        }
        if (flag == 0 && fraudDetectionService != null) {
            fraudDetectionService.flagTransaction(username);
        }
        return null;
    }

    // Method to return the list of all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users); // Return a copy of the list to prevent modification
    }

    // Method to update user details
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                return;
            }
        }
        // Handle case where user does not exist, e.g., throw an exception or log an error
    }
}
