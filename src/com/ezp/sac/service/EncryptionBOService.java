//Author: Bhavansh
package com.ezp.sac.service;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.*;

import java.util.List;

public class EncryptionBOService {

    private final EncryptionBO encryptionBO;
    private final UserBO userBO;

    // Constructor that initializes EncryptionBO and UserBO
    public EncryptionBOService() {
        userBO = UserBO.getInstance(); // Use singleton instance of UserBO
        encryptionBO = new EncryptionBO(userBO); // Pass UserBO to EncryptionBO
    }

    public User encryptUserData(String encryptionAlgorithm, String username) {
        User encryptedUser = encryptionBO.encryptUserData(encryptionAlgorithm, username);
        if (encryptedUser != null) {
            return encryptedUser; // Return the encrypted user if encryption is successful
        }
        return null; // Return null if encryption fails
    }

    // Method to retrieve the list of all users
    public List<User> getAllUsers() {
        return userBO.getAllUsers();
    }
}