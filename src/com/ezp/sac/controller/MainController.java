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
        EncryptionBOService encryptionBOService = new EncryptionBOService();
        DecryptionBOService decryptionBOService = new DecryptionBOService();
        UserBO userBO = UserBO.getInstance();
        FraudDetectionService fraudDetectionService = new FraudDetectionService(userBO);

        
        

        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("All users list:");
            List<User> allUsers = encryptionBOService.getAllUsers();
            for (User user : allUsers) {
                System.out.println(user);
            }

            while (true) {
                System.out.println("\nEnter the number corresponding to the algorithm you want to use:");
                System.out.println("Encryption Algorithms:");
                System.out.println("1: Proceed transaction with Vernam Cipher (One-Time Pad) Concept");
                System.out.println("0: Exit");

                System.out.print("\nEnter your choice: ");
                String choice = reader.readLine();

                if (choice.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }

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

    private static void handleEncryption(EncryptionBOService encryptionBOService, DecryptionBOService decryptionBOService, BufferedReader reader, FraudDetectionService fraudDetectionService) throws IOException {
        String encryptionAlgorithm = "Vernam Cipher";
        int count = 0;
        System.out.print("\nEnter the username of the user you want to see the Transactions for: ");
        String username = reader.readLine();

        User encryptedUser = encryptionBOService.encryptUserData(encryptionAlgorithm, username);
        
        if (encryptedUser != null) {
            System.out.println("Enter the password: ");
        	String password = reader.readLine();

            User checkPasswordUser = fraudDetectionService.checkPassword(username,password,true);
        	while(checkPasswordUser== null){
        		if(count>1) {
        			User decryptedUser = decryptionBOService.decryptUserData(encryptionAlgorithm, encryptedUser.getUsername());
        			break;
        		}
                System.out.println("Enter the password again: ");
        		password = reader.readLine();
                checkPasswordUser = fraudDetectionService.checkPassword(username,password,true);
        		count++;
        	}
            if(checkPasswordUser!= null){
                System.out.println("\nEncrypted User: " + encryptedUser);
                

                User decryptedUser = decryptionBOService.decryptUserData(encryptionAlgorithm, encryptedUser.getUsername());
            
                if (decryptedUser != null) {
                    System.out.println("Decrypted User: " + decryptedUser);
                } else {
                    System.out.println("Decryption failed.");
                }

                }
            else{
                System.out.println("Transaction flagged as fraudulent for : " + username );
            }

            }
            
    }
}