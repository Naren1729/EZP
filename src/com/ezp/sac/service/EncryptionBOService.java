/**
 * @Author: Bhavansh
 * @Date : 11/08/2024
 * 
 * @Description:
 * The EncryptionBOService class acts as a service layer that manages the 
 * encryption of user data. It integrates with the EncryptionBO and UserBO classes 
 * to securely encrypt user information based on a specified encryption algorithm. 
 * Additionally, this service provides a method to retrieve the list of all users 
 * within the system, ensuring that the encryption process is accessible and centralized.
 */
package com.ezp.sac.service;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.*;

import java.util.List;

public class EncryptionBOService {

    private final EncryptionBO encryptionBO;
    private final UserBO userBO;
    private FraudDetectionService fraudDetectionService;

    // Constructor that initializes EncryptionBO and UserBO
    public EncryptionBOService() {
        userBO = UserBO.getInstance(); // Use singleton instance of UserBO
        encryptionBO = new EncryptionBO(userBO); // Pass UserBO to EncryptionBO
        fraudDetectionService = new FraudDetectionService(userBO);
    }
    

    public User encryptUserData(String encryptionAlgorithm, String username) {
        User encryptedUser = encryptionBO.encryptUserData(encryptionAlgorithm, username);
        if (encryptedUser != null) {
            return encryptedUser; // Return the encrypted user if encryption is successful
        }
        else{
            fraudDetectionService.flagTransaction(username);
        }
        return null; // Return null if encryption fails
    }

    // Method to retrieve the list of all users
    public List<User> getAllUsers() {
        return userBO.getAllUsers();
    }
}