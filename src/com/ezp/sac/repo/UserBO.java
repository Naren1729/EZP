package com.ezp.sac.repo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ezp.sac.model.User;

public class UserBO {
    private List<User> users;
    private boolean initialized = false;
    
    // Singleton instance
    private static UserBO instance;

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
            initialized = true; // Set flag to true after initialization
        }
    }

    // Method to get a user by their username
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // Return null if the user is not found
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
