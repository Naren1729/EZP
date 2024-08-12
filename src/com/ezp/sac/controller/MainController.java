/**
 * @Author : Bhavansh, Naren Sri Sai, Arvind
 * @Date : 11/08/2024
 * 
 * @Description:
 * This is the main controller class for the encryption and decryption service application.
 * It interacts with the user through the console, allowing the selection of encryption 
 * algorithms and the processing of user data. The Vernam Cipher (One-Time Pad) is currently 
 * implemented for encryption and decryption. The application also includes a fraud detection 
 * service that can be extended for further functionality.
 */

package com.ezp.sac.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.ezp.sac.model.User;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.service.EncryptionBOService;
import com.ezp.sac.service.DecryptionBOService;
import com.ezp.sac.service.FraudDetectionService;

public class MainController {

    public static void main(String[] args) { 
    	
    	/**
         * Initializing services and repository
         * 
         * @param encryptionBOService The service used for encryption.
         * @param decryptionBOService The service used for decryption.
         * @param reader BufferedReader for reading user input.
         * @param fraudDetectionService Service for detecting fraudulent transactions.
         * @throws IOException If an input or output exception occurs.
         */
    	
        EncryptionBOService encryptionBOService = new EncryptionBOService();
        DecryptionBOService decryptionBOService = new DecryptionBOService();
        UserBO userBO = UserBO.getInstance();
        FraudDetectionService fraudDetectionService = new FraudDetectionService(userBO);
        userBO.setFraudDetectionService(fraudDetectionService);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            // Display all users in the system
            System.out.println("All users list:");
            List<User> allUsers = encryptionBOService.getAllUsers();
            for (User user : allUsers) {
                System.out.println(user);
            }

            // Menu-driven interaction for user input
            while (true) {
                System.out.println("\nEnter the number corresponding to the algorithm you want to use:");
                System.out.println("Encryption Algorithms:");
                System.out.println("1: Vernam Cipher (One-Time Pad) Concept");
                System.out.println("0: Exit");

                System.out.print("\nEnter your choice: ");
                String choice = reader.readLine();

                // Exit condition
                if (choice.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Handling user choice for encryption
                switch (choice) {
                    case "1":
                        handleEncryption(encryptionBOService, decryptionBOService, reader, fraudDetectionService);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            // Handling I/O exceptions
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    private static void handleEncryption(EncryptionBOService encryptionBOService, DecryptionBOService decryptionBOService, BufferedReader reader, FraudDetectionService fraudDetectionService) throws IOException {
        String encryptionAlgorithm = "Vernam Cipher";
        System.out.print("\nEnter the username of the user you want to encrypt: ");
        String username = reader.readLine();

        // Encrypt user data
        User encryptedUser = encryptionBOService.encryptUserData(encryptionAlgorithm, username);
        if (encryptedUser != null) {
            System.out.println("\nEncrypted User: " + encryptedUser);

            // Decrypt user data
            User decryptedUser = decryptionBOService.decryptUserData(encryptionAlgorithm, encryptedUser.getUsername());
            
            if (decryptedUser != null) {
                System.out.println("Decrypted User: " + decryptedUser);
            } else {
                System.out.println("Decryption failed.");
            }

            // Optional fraud detection processing
            // processFraudDetection(fraudDetectionService);
        } else {
            System.out.println("Invalid username.");
        }
    }

    /*
    private static void processFraudDetection(FraudDetectionService fraudDetectionService) {
        // Example transactions
        String[] transactions = {"activity1", "activity3"};

        for (String transaction : transactions) {
            fraudDetectionService.flagTransaction(transaction);
        }
    }
    */
}
