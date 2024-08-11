package com.ezp.sac.repo;

import java.util.*;
import com.ezp.sac.model.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class DataEncryptionRepository {
    // List of users to simulate a data repository
    public List<User> users;

    // Encryption key used for the encryption/decryption process
    public final String encryptionKey = "KEY";

    // Encryption algorithm used in this repository (Vernam Cipher)
    public String encryptionAlgorithm = "Vernam Cipher";

    // Default constructor that initializes dummy users
    public DataEncryptionRepository() {
        initializeDummyUsers();
    }
    
    // Method to initialize a list of dummy users with predefined data
    public void initializeDummyUsers() {
        users = new ArrayList<>();
        users.add(new User("johnDoe", "John Doe", "password123", 1001L, "deposit", 250.75, LocalDateTime.now(), "completed"));
        users.add(new User("janeSmith", "Jane Smith", "password456", 1002L, "withdrawal", 100.50, LocalDateTime.now(), "pending"));
        users.add(new User("aliceJones", "Alice Jones", "password789", 1003L, "deposit", 500.00, LocalDateTime.now(), "completed"));
        users.add(new User("bobBrown", "Bob Brown", "password012", 1004L, "transfer", 150.25, LocalDateTime.now(), "failed"));
        users.add(new User("carolWhite", "Carol White", "password345", 1005L, "deposit", 75.00, LocalDateTime.now(), "completed"));
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
        return users;
    }

    // Method to encrypt user data based on the specified encryption algorithm
    public User encryptUserData(String encryptionAlgorithm, String username) {
        if (encryptionAlgorithm.equals(this.encryptionAlgorithm)) { // Check if the encryption algorithm matches
            User user = getUserByUsername(username);
            
            if (user != null) {
                // Encrypt each field of the user object
                user.setUsername(encrypt(user.getUsername()));
                user.setName(encrypt(user.getName()));
                user.setPassword(encrypt(user.getPassword()));
                user.setTransaction_id(encryptLong(user.getTransaction_id())); 
                user.setType(encrypt(user.getType()));
                user.setAmount(encryptDouble(user.getAmount())); // Encrypt the amount
                user.setDate(user.getDate()); // Date is not encrypted
                user.setStatus(encrypt(user.getStatus()));
            }
            return user;
        }
        return null; // Return null if the encryption algorithm does not match
    }

    // Method to decrypt user data based on the specified encryption algorithm
    public User decryptUserData(String encryptionAlgorithm, String username) {
        if (encryptionAlgorithm.equals(this.encryptionAlgorithm)) { // Check if the encryption algorithm matches
            User user = getUserByUsername(username);
            
            if (user != null) {
                // Decrypt each field of the user object
                user.setUsername(decrypt(user.getUsername()));
                user.setName(decrypt(user.getName()));
                user.setPassword(decrypt(user.getPassword()));
                user.setTransaction_id(decryptLong(user.getTransaction_id()));
                user.setType(decrypt(user.getType()));
                user.setAmount(decryptDouble(user.getAmount())); // Decrypt the amount
                user.setDate(user.getDate()); // Date is not decrypted
                user.setStatus(decrypt(user.getStatus()));
            }
            return user;
        }
        return null; // Return null if the encryption algorithm does not match
    }

    // Method to encrypt a Long value using the encryption key
    public Long encryptLong(Long val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long encrypted = val;
        
        for (int i = 0; i < keyBytes.length; i++) {
            encrypted ^= keyBytes[i]; // XOR each byte of the key with the Long value
            encrypted = Long.rotateLeft(encrypted, 8); // Rotate bits to mix up encryption
        }
        
        return encrypted;
    }

    // Method to decrypt a Long value using the encryption key
    public Long decryptLong(Long val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long decrypted = val;
        
        for (int i = keyBytes.length - 1; i >= 0; i--) {
            decrypted = Long.rotateRight(decrypted, 8); // Rotate bits back
            decrypted ^= keyBytes[i]; // XOR each byte of the key with the encrypted value
        }
        
        return decrypted;
    }

    // Method to encrypt a double value using the encryption key
    public double encryptDouble(double val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long longBits = Double.doubleToLongBits(val);
        long encrypted = longBits;
        
        for (int i = 0; i < keyBytes.length; i++) {
            encrypted ^= keyBytes[i]; // XOR each byte of the key with the long bits of the double value
            encrypted = Long.rotateLeft(encrypted, 8); // Rotate bits to mix up encryption
        }
        
        return Double.longBitsToDouble(encrypted);
    }

    // Method to decrypt a double value using the encryption key
    public double decryptDouble(double val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long longBits = Double.doubleToLongBits(val);
        long decrypted = longBits;
        
        for (int i = keyBytes.length - 1; i >= 0; i--) {
            decrypted = Long.rotateRight(decrypted, 8); // Rotate bits back
            decrypted ^= keyBytes[i]; // XOR each byte of the key with the encrypted value
        }
        
        return Double.longBitsToDouble(decrypted);
    }

    // Method to encrypt a String value using Vernam Cipher and Base64 encoding
    public String encrypt(String val) {
        StringBuilder result = new StringBuilder();
    
        for (int i = 0; i < val.length(); i++) { 
            char actual_char = val.charAt(i);  
            char key_char = encryptionKey.charAt(i % encryptionKey.length()); 
            result.append((char) (actual_char ^ key_char)); // XOR each character with the key character
        }
        String encrypted = Base64.getEncoder().encodeToString(result.toString().getBytes()); // Encode the result with Base64

        return encrypted;
    }

    // Method to decrypt a String value using Base64 decoding and Vernam Cipher
    public String decrypt(String val) {
        byte[] decodedBytes = Base64.getDecoder().decode(val);
        StringBuilder result = new StringBuilder();
        String decodedString = new String(decodedBytes);
        
        for (int i = 0; i < decodedString.length(); i++) {
            char actual_char = decodedString.charAt(i);
            char key_char = encryptionKey.charAt(i % encryptionKey.length());  
            result.append((char) (actual_char ^ key_char)); // XOR each character with the key character
        }

        String decrypted = result.toString();
        return decrypted;
    }
}
