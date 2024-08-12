//Authors: Bhavansh, Keerthana
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