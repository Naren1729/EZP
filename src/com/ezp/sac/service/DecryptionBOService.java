/**
 * @Authors : Bhavansh, Keerthana
 * @Date : 11/08/2024
 * 
 * @Description:
 * The DecryptionBOService class serves as a service layer that facilitates the 
 * decryption of user data. It integrates with the DecryptionBO and UserBO classes 
 * to retrieve and decrypt user information based on the provided encryption algorithm 
 * and username. This service ensures that the decryption process is centralized and 
 * can be easily accessed within the application.
 */

package com.ezp.sac.service;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.*;

public class DecryptionBOService {

    private final DecryptionBO decryptionBO;
    private final UserBO userBO;

    // Constructor that initializes DecryptionBO and UserBO
    public DecryptionBOService() {
        userBO = UserBO.getInstance(); // Use singleton instance
        decryptionBO = new DecryptionBO(userBO); // Pass UserBO to DecryptionBO
    }

    public User decryptUserData(String encryptionAlgorithm, String username) {
        User decryptedUser = decryptionBO.decryptUserData(encryptionAlgorithm, username);
        if (decryptedUser != null) {
            return decryptedUser; // Return the decrypted user if decryption is successful
        }
        return null; // Return null if decryption fails
    }
}