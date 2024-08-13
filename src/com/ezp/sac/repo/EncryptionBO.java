/**
 * @Author : Bhavansh
 * @Date : 11/08/2024
 * 
 * @Description:
 * This class handles the encryption of user data using the Vernam Cipher algorithm. 
 * It encrypts various fields of the User object, including username, name, password, 
 * transaction ID, type, amount, and status. The class interacts with the UserBO repository 
 * to retrieve and update user data. The encryption process uses a predefined encryption key 
 * and includes methods for encrypting Long, Double, and String values.
 */
package com.ezp.sac.repo;

import java.util.Base64;
import com.ezp.sac.model.User;

public class EncryptionBO {
    private final String encryptionKey = "KEY";
    private final String encryptionAlgorithm = "Vernam Cipher";
    
    private final UserBO userBO;
    
    // Constructor that initializes UserBO
    public EncryptionBO(UserBO userBO) {
        this.userBO = userBO;
    }
    
    // Method to encrypt user data using the specified encryption algorithm
    public User encryptUserData(String encryptionAlgorithm, String username) {
        if (this.encryptionAlgorithm.equals(encryptionAlgorithm)) {
            User user = userBO.getUserByUsername(username);
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

                // Update the encrypted user back to UserBO
                userBO.updateUser(user);
               
                return user;
            }
        }
        return null; // Return null if encryption fails
    }

    // Encrypt Long value
    public Long encryptLong(Long val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long encrypted = val;
        
        for (int i = 0; i < keyBytes.length; i++) {
            encrypted ^= keyBytes[i];
            encrypted = Long.rotateLeft(encrypted, 8);
        }
        
        return encrypted;
    }

    // Encrypt Double value
    public double encryptDouble(double val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long longBits = Double.doubleToLongBits(val);
        long encrypted = longBits;
        
        for (int i = 0; i < keyBytes.length; i++) {
            encrypted ^= keyBytes[i];
            encrypted = Long.rotateLeft(encrypted, 8);
        }
        
        return Double.longBitsToDouble(encrypted);
    }

    // Encrypt String value
    public String encrypt(String val) {
        StringBuilder result = new StringBuilder();
    
        for (int i = 0; i < val.length(); i++) {
            char actualChar = val.charAt(i);
            char keyChar = encryptionKey.charAt(i % encryptionKey.length());
            result.append((char) (actualChar ^ keyChar));
        }
        return Base64.getEncoder().encodeToString(result.toString().getBytes());
    }
}