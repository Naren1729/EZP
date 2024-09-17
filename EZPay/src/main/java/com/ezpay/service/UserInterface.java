/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * @description:
 * Interface for user-related operations in the service layer.
 * Provides methods for user management, including retrieval, creation, update, and authentication.
 */

package com.ezpay.service;

import java.util.List;

import com.ezpay.entity.User;

public interface UserInterface {
    
    /**
     * Retrieves all users.
     * @return List of all users.
     */
    public List<User> findAll();
    
    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user.
     * @return The user with the specified ID.
     */
    public User findById(Long id);
    
    /**
     * Saves a new user to the system.
     * @param user The user to be saved.
     * @return The saved user.
     */
    public User saveUser(User user);
    
    /**
     * Updates an existing user's details.
     * @param user The user with updated details.
     * @return The updated user.
     */
    public User updateUser(Long userID,User user);
    
    /**
     * Checks if a username already exists in the system.
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    public boolean checkUsername(String username);
    
    /**
     * Checks if a username and password combination is valid.
     * @param username The username to check.
     * @param password The password to verify.
     * @return true if the username and password match, false otherwise.
     */
    public boolean checkPassword(String username, String password);
    
    /**
     * Authenticates a user based on their username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if the credentials are valid, false otherwise.
     */
    public boolean authenticate(String username, String password);
}

