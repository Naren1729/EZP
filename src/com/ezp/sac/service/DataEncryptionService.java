package com.ezp.sac.service;

import java.util.*;

import com.ezp.sac.model.*;
import com.ezp.sac.repo.*;

public class DataEncryptionService {

    private DataEncryptionRepository dataEncryptionRepository;
    
    // Constructor that initializes the DataEncryptionRepository
    public DataEncryptionService() {
        dataEncryptionRepository = new DataEncryptionRepository();
    }
    
    // Method to encrypt user data using the specified encryption algorithm
    public User encryptUserData(String encryptionAlgorithm, String username) {
        User encrypted_user = dataEncryptionRepository.encryptUserData(encryptionAlgorithm, username);
        if (encrypted_user != null) {
            return encrypted_user; // Return the encrypted user if encryption is successful
        }
        return null; // Return null if encryption fails
    }
    
    // Method to decrypt user data using the specified encryption algorithm
    public User decryptUserData(String encryptionAlgorithm, String username) {
        User decrypted_user = dataEncryptionRepository.decryptUserData(encryptionAlgorithm, username);
        if (decrypted_user != null) {
            return decrypted_user; // Return the decrypted user if decryption is successful
        }
        return null; // Return null if decryption fails
    }
    
    // Method to retrieve the list of all users
    public List<User> getAllUsers() {
        return dataEncryptionRepository.getAllUsers();
    }
}
