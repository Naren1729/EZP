/**
 * @Authors : Keerthana B, Bhavansh, Naren Sri Sai, Arvind
 * @Date : 19/08/2024
 * 
 * @Description:
 * This is the main controller class for the encryption and decryption service application.
 * This is achieved by using a RDBMS (Oracle SQL) connected to the java program using JDBC.
 * It interacts with the user through the console, allowing the selection of encryption 
 * algorithms and the processing of user data. The Vernam Cipher (One-Time Pad) is currently 
 * implemented for encryption and decryption. The application also includes a fraud detection 
 * service that can be extended for further functionality.
 */


/**
 * @BeforeExecution: Run the attached SQL file's command under Oracle SQL Command Line Window
 */

package com.ezp.sac.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.service.EncryptionBOService;
import com.ezp.sac.service.DecryptionBOService;
import com.ezp.sac.service.FraudDetectionService;

public class MainController {

    public static void main(String[] args) {
        // Initialize the services
        EncryptionBOService encryptionBOService = new EncryptionBOService();
        DecryptionBOService decryptionBOService = new DecryptionBOService();
        UserBO userBO = UserBO.getInstance();
        FraudDetectionService fraudDetectionService = new FraudDetectionService();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
    		// Display table contents
    		System.out.println();
    		System.out.println("Table of all users:");
    		System.out.println();
    		encryptionBOService.displayUserTable();
    		

            // Main loop for handling user input
            while (true) {
                System.out.println("\nEnter the number corresponding to the algorithm you want to use:");
                System.out.println("Encryption Algorithms:");
                System.out.println("1: Proceed transaction with Vernam Cipher (One-Time Pad) Concept");
                System.out.println("0: Exit");

                System.out.print("\nEnter your choice: ");
                String choice = reader.readLine();

                // Exit condition
                if (choice.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Handle the user's choice
                switch (choice) {
                    case "1":
                        handleEncryption(encryptionBOService, decryptionBOService, reader, fraudDetectionService);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the encryption and decryption process for a given user.
     * 
     * @param encryptionBOService    Service for encrypting user data.
     * @param decryptionBOService    Service for decrypting user data.
     * @param reader                 BufferedReader for reading user input.
     * @param fraudDetectionService  Service for detecting fraudulent transactions.
     * @throws IOException           In case of an input/output error.
     */
    
    private static void handleEncryption(EncryptionBOService encryptionBOService, DecryptionBOService decryptionBOService, BufferedReader reader, FraudDetectionService fraudDetectionService) throws IOException {
        String encryptionAlgorithm = "Vernam Cipher";
        int loop = 3;

        // Prompt for the username to view transactions
        System.out.print("\nEnter the username of the user you want to see the Transactions for: ");
        String username = reader.readLine();
        
        
        boolean isCorrectUsername = fraudDetectionService.checkUsername(username);
        if(isCorrectUsername==false) {
        	fraudDetectionService.flagTransactionUsername(username);
        	return;
        }
     // Prompt for the password to view transactions
        System.out.println("Enter the password of the user you want to see the Transactions for: ");
        String password = reader.readLine();
        boolean isCorrectPassword = fraudDetectionService.checkPassword(password);
        
        if(isCorrectPassword==false) {
        	int count = 2;
        	while(count>=0 && isCorrectPassword==false) {
        		System.out.println("The password is incorrect please try again: ");
        		password = reader.readLine();
                isCorrectPassword = fraudDetectionService.checkPassword(password);
                count--;
        	}
        }
       
        if(isCorrectUsername && isCorrectPassword) {
	        // Encrypt the user's data
	        User encryptedUser = encryptionBOService.encryptUserData(encryptionAlgorithm, username);
	        // Displaying encrypted record
	        if (encryptedUser != null) {
	            System.out.println("\nEncrypted User: " + encryptedUser);
	            
	            // Decrypt the user's data
	            User decryptedUser = decryptionBOService.decryptUserData(encryptionAlgorithm, encryptedUser.getUsername());
	            
	            // Displaying decrypted record
	            if (decryptedUser != null) {
	                System.out.println("Decrypted User: " + decryptedUser);
	            } else {
	                System.out.println("Decryption failed.");
	            }
	
	        } else {
	            System.out.println("Invalid username or password");
	        }
        }
        else {
        	System.out.println("Either username or password is incorrect");
        }
    }
}
