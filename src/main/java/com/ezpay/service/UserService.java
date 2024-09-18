/**
 * @author: Naren, Bhavansh
 * @date: 3/09/2024
 * 
 * @description:
 * Service class for managing user-related operations.
 * Implements the UserInterface to provide methods for user retrieval, creation, update, and authentication.
 */

package com.ezpay.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import com.ezpay.entity.User;
import com.ezpay.exception.EncryptionOrDecryptionException;
import com.ezpay.repository.UserRepo;


@Service
public class UserService implements UserInterface {
    
    @Autowired
    private UserRepo userRepo; // Repository for user data

    /**
     * Retrieves all users from the repository and decrypts their details.
     * @return List of all decrypted users.
     */
    public List<User> findAll() {
        // Fetch encrypted users from the repository
        List<User> encryptedUsers = userRepo.findAll();
        List<User> decryptedUsers = new ArrayList<>();
        for (User user : encryptedUsers) {
            // Decrypt each user
//            decryptedUsers.add(decryptionservice.decryptUser(user));
        	decryptedUsers.add(user);
        }
        return decryptedUsers;
    }

    /**
     * Retrieves a user by their ID and decrypts their details.
     * @param id The ID of the user.
     * @return The decrypted user with the specified ID, or null if not found.
     */
    public User findById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            // Decrypt the user if found
//            return decryptionservice.decryptUser(user.get());
        	return user.get();
        }
        return null;
    }

    
    public User findByUsername(String  username) {
    	String encrypterusername = username;
        Optional<User> user = userRepo.findByUsername(encrypterusername);
        if (user.isPresent()) {
            // Decrypt the user if found
//            return decryptionservice.decryptUser(user.get());
        	return user.get();
        }
        return null;
    }
    /**
     * Saves a new user to the repository after encrypting their details.
     * @param user The user to be saved.
     * @return The encrypted user that was saved.
     */
    public User saveUser(User user) {
    	if(user.getUsername() == "") {
    		return null;
    	}
        try {
            // Encrypt the user before saving
            //User encryptedUser = encryptionservice.encryptUser(user);
            // Save the encrypted user
            userRepo.save(user);
            return user;
        } catch (Exception e) {
            // Log the exception (optional)
            System.err.println("Error saving user: " + e.getMessage());
            // Handle the exception as needed
            return null;
        }
    }


    /**
     * Updates an existing user's details and saves the updated user.
     * @param user The user with updated details.
     * @return The updated user.
     */
    public User updateUser(Long userID,User user) {
//        long userID = user.getId();
        User user1 = findById(userID);
        if (user1 != null) {
            // Update user details
            user1.setCurrentBalance(user.getCurrentBalance());
            user1.setEmail(user.getEmail());
            user1.setPassword(user1.getPassword()); // Assuming password should not be updated here
            user1.setUsername(user.getUsername());
            user1.setTransactionPassword(user.getTransactionPassword());
            user1.setBlockeListed(user.getIsBlockeListed());
            saveUser(user1);
        }
        return user1;
    }
    
    
    public User changeUser(Long userID, Map<String, Object> updates) {
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new EncryptionOrDecryptionException("User not found with id: " + userID));
        
        updates.forEach((field, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(User.class, field);
            fieldToBeUpdated.setAccessible(true);

            Class<?> fieldType = fieldToBeUpdated.getType();
            Object valueToSet = value;

            // Convert value if necessary
            if (fieldType.equals(BigDecimal.class) && value instanceof Number) {
                valueToSet = new BigDecimal(((Number) value).toString());
            }
//            } else if (fieldType.equals(Integer.class) && value instanceof Number) {
//                valueToSet = ((Number) value).intValue();
//            } // Add other conversions as needed
            
            ReflectionUtils.setField(fieldToBeUpdated, user, valueToSet);
        });
        
        return userRepo.save(user);
    }

    /**
     * Checks if a username exists in the system.
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    public boolean checkUsername(String username) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
            	System.out.println(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the provided password matches the stored password for the given username.
     * @param username The username to check.
     * @param password The password to verify.
     * @return true if the password matches, false otherwise.
     */
    public boolean checkPassword(String username, String password) {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            User encryptedUser = user.get();
            // Compare the provided password with the stored password
            return encryptedUser.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Authenticates a user based on their username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if the username and password are valid, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        // Check if the username and password are valid
        return checkUsername(username) && checkPassword(username, password);
    }

	public boolean deleteUsers(Long id) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {

            // Compare the provided password with the stored password
            userRepo.deleteById(id);
            return true;
        }
        return false;
	}
}
