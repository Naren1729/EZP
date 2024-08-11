package com.ezp.sac.repo;

import java.util.Base64;
import com.ezp.sac.model.User;

public class DecryptionBO {
    private final String encryptionKey = "KEY";
    private final String encryptionAlgorithm = "Vernam Cipher";
    
    private final UserBO userBO;
    
    // Constructor that initializes UserBO
    public DecryptionBO(UserBO userBO) {
        this.userBO = userBO; // Use the provided UserBO instance
    }

    public User decryptUserData(String encryptionAlgorithm, String username) {
        if (this.encryptionAlgorithm.equals(encryptionAlgorithm)) {
            User user = userBO.getUserByUsername(username);
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
                return user;
            }
        }
        return null; // Return null if the encryption algorithm does not match
    }

    // Decrypt Long value
    public Long decryptLong(Long val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long decrypted = val;
        
        for (int i = keyBytes.length - 1; i >= 0; i--) {
            decrypted = Long.rotateRight(decrypted, 8); // Rotate bits back
            decrypted ^= keyBytes[i]; // XOR each byte of the key with the encrypted value
        }
        
        return decrypted;
    }

    // Decrypt Double value
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

    // Decrypt String value
    public String decrypt(String val) {
        byte[] decodedBytes = Base64.getDecoder().decode(val);
        StringBuilder result = new StringBuilder();
        String decodedString = new String(decodedBytes);
        
        for (int i = 0; i < decodedString.length(); i++) {
            char actualChar = decodedString.charAt(i);
            char keyChar = encryptionKey.charAt(i % encryptionKey.length());  
            result.append((char) (actualChar ^ keyChar)); // XOR each character with the key character
        }

        return result.toString();
    }
}
