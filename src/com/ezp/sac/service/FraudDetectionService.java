/**
 * @Authors: Naren Sri Sai, Arvind
 * @Date : 11/08/2024
 * 
 * @Description:
 * The FraudDetectionService class is responsible for detecting and flagging potentially
 * fraudulent transactions based on username similarity analysis. It utilizes a custom
 * Jaccard similarity calculation to compare transaction details with existing usernames
 * in the system, determining a risk score. Depending on the calculated risk score, the 
 * service can flag transactions as fraudulent or incorrect, ensuring enhanced security 
 * and reliability in transaction processing.
 */


package com.ezp.sac.service;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.model.*;
import java.util.List;

public class FraudDetectionService {


    private final FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();
    private final UserBO userBO;
    
    // Constructor that initializes the FraudDetectionService with UserBO
    public FraudDetectionService(UserBO userBO) {
        this.userBO = userBO;
    }

    // Method to get the list of suspicious usernames
    public List<String> getUsername() {
        return fraudDetectionSystem.getUsername();
    }

    // Method to set the list of suspicious usernames
    public void setUsername(List<String> username) {
        fraudDetectionSystem.setUsername(username);
    }
    
    //Finding the maximum similarity it has with the username in the backend
    public void detectFraud(String transaction) {
        List<User> suspiciousActivities = userBO.getAllUsers();
        double highestSimilarity = 0;

        for (User user : suspiciousActivities) {
            String username = user.getUsername();
            double similarity = calculateSimilarity(transaction, username);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
            }
        }

        fraudDetectionSystem.setRiskScore(1 - highestSimilarity);
    }
    
    //Comparing the risk score and output for failed transaction
    public void flagTransaction(String transaction) {
        detectFraud(transaction);

        double riskScore = fraudDetectionSystem.getRiskScore();
        if (riskScore > 0.3) {
            System.out.println("Transaction flagged as fraudulent: " + transaction + " with a risk score: " + riskScore);
        } else if (riskScore > 0) {
            System.out.println("The entered username: " + transaction + " is incorrect with a risk score: " + riskScore);
        } else {
            System.out.println("Transaction is safe: " + transaction + " with a risk score: " + riskScore);
        }
    }
    
    //Jaccard similarity 
    public double calculateSimilarity(String str1, String str2) {
        int intersection = 0;
        int union = str1.length() + str2.length();

        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) != -1) {
                intersection++;
            }
        }

        return (double) intersection / (union - intersection);
    }
}
