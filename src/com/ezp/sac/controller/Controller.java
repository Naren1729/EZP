package com.ezp.sac.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.ezp.sac.model.User;
import com.ezp.sac.service.DataEncryptionService;
import com.ezp.sac.service.FraudDetectionService;

public class Controller {

    public static void main(String[] args) {
        DataEncryptionService dataEncryptionService = new DataEncryptionService();
        FraudDetectionService fraudDetectionService = new FraudDetectionService();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("All users list:");
            List<User> allUsers = dataEncryptionService.getAllUsers();
            for (User user : allUsers) {
                System.out.println(user);
            }

            while (true) {
                System.out.println("\nEnter the number corresponding to the algorithm you want to use:");
                System.out.println("Encryption Algorithms:");
                System.out.println("1: Vernam Cipher (One-Time Pad) Concept");
                System.out.println("0: Exit");

                System.out.print("\nEnter your choice: ");
                String choice = reader.readLine();

                if (choice.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }

                switch (choice) {
                    case "1":
                        handleEncryption(dataEncryptionService, reader, fraudDetectionService);
                        break;

                    default:
                        System.out.println("Invalid choice. Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleEncryption(DataEncryptionService dataEncryptionService, BufferedReader reader, FraudDetectionService fraudDetectionService) throws IOException {
        String encryptionAlgorithm = "Vernam Cipher";
        System.out.print("\nEnter the username of the user you want to encrypt: ");
        String username = reader.readLine();

        User encryptedUser = dataEncryptionService.encryptUserData(encryptionAlgorithm, username);
        if (encryptedUser != null) {
            System.out.println("\nEncrypted User: " + encryptedUser);

            User decryptedUser = dataEncryptionService.decryptUserData(encryptionAlgorithm, encryptedUser.getUsername());
            if (decryptedUser != null) {
                System.out.println("Decrypted User: " + decryptedUser);
            } else {
                System.out.println("Decryption failed.");
            }

            processFraudDetection(fraudDetectionService);
        } else {
            System.out.println("Invalid username.");
        }
    }

    private static void processFraudDetection(FraudDetectionService fraudDetectionService) {
        // Example transactions
        String[] transactions = {"activity1", "activity3"};

        for (String transaction : transactions) {
            fraudDetectionService.flagTransaction(transaction);
        }
    }
}
