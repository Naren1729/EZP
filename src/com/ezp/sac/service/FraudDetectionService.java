package com.ezp.sac.service;

import com.ezp.sac.repo.FraudDetectionBO;
import com.ezp.sac.model.*;
import java.util.List;

/**
 * Service class for fraud detection.
 */
public class FraudDetectionService {

    // Instance of FakeDatabase to simulate a database of suspicious activities.
    private final FraudDetectionBO fakeDatabase = new FraudDetectionBO();
    // Instance of FraudDetectionSystem to manage fraud detection logic.
    private final FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();

    /**
     * Gets the list of usernames from the fraud detection system.
     * @return List of usernames.
     */
    public List<String> getUsername() {
        return fraudDetectionSystem.getUsername();
    }

    /**
     * Sets the list of usernames in the fraud detection system.
     * @param username List of usernames.
     */
    public void setUsername(List<String> username) {
        fraudDetectionSystem.setUsername(username);
    }

    /**
     * Detects fraud by comparing the given transaction with suspicious activities.
     * @param transaction The transaction to be checked for fraud.
     */
    public void detectFraud(String transaction) {
        List<String> suspiciousActivities = fakeDatabase.getUsername();
        double highestSimilarity = 0;

        // Calculate similarity between the transaction and each suspicious activity.
        for (String activity : suspiciousActivities) {
            double similarity = calculateSimilarity(transaction, activity);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
            }
        }

        // Set the risk score based on the highest similarity found.
        fraudDetectionSystem.setRiskScore(1 - highestSimilarity);
    }

    /**
     * Flags a transaction as fraudulent or safe based on the risk score.
     * @param transaction The transaction to be flagged.
     */
    public void flagTransaction(String transaction) {
        detectFraud(transaction);

        double riskScore = fraudDetectionSystem.getRiskScore();
        if (riskScore > 0.3) {
            System.out.println("Transaction flagged as fraudulent: " + transaction+" with a riskscore : "+riskScore);
        } else if (riskScore > 0) {
            System.out.println("The entered username: " + transaction +" with a riskscore : "+ riskScore);
        } else {
            System.out.println("Transaction is safe: " + transaction+" with a riskscore : "+riskScore);
        }
    }

    /**
     * Calculates the similarity between two strings using a simple intersection-over-union method.
     * @param str1 The first string.
     * @param str2 The second string.
     * @return The similarity score between the two strings.
     */
    private double calculateSimilarity(String str1, String str2) {
        int intersection = 0;
        int union = str1.length() + str2.length();

        // Calculate the intersection of characters between the two strings.
        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) != -1) {
                intersection++;
            }
        }

        // Return the similarity score.
        return (double) intersection / (union - intersection);
    }
}
