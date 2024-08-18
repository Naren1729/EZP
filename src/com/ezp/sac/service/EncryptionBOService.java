/**
 * @Authors : Keerthana B, Bhavansh
 * @Date : 19/08/2024
 * 
 * @Description:
 * This class of EncryptionBOService acts as an intermediate to the 
 * EncryptionBO and UserBO classes where the actual business logic 
 * for encryption of the user details is written by aiding as a service layer.
 */
package com.ezp.sac.service;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.*;

public class EncryptionBOService {

	private final EncryptionBO encryptionBO;
	private final UserBO userBO;
//	private FraudDetectionService fraudDetectionService;

	// Constructor that initializes EncryptionBO and UserBO
	public EncryptionBOService() {
		userBO = UserBO.getInstance(); // Use singleton instance of UserBO
		encryptionBO = new EncryptionBO(userBO); // Pass UserBO to EncryptionBO
//		fraudDetectionService = new FraudDetectionService(userBO);
	}

	public User encryptUserData(String encryptionAlgorithm, String username) {
		User encryptedUser = encryptionBO.encryptUserData(encryptionAlgorithm, username);
		if (encryptedUser != null) {
			return encryptedUser; // Return the encrypted user if encryption is successful
		}
//        else{
//            fraudDetectionService.flagTransaction(username);
//        }
		return null; // Return null if encryption fails
	}

	// Method to display the table of all users
	public void displayUserTable() {
		userBO.displayUserTable();
	}
}