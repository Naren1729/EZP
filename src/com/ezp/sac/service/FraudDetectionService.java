package com.ezp.sac.service;

import com.ezp.sac.repo.FraudDetectionBO;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.model.*;
import java.util.List;

public class FraudDetectionService {

    private final FraudDetectionBO fakeDatabase = new FraudDetectionBO();
    private final FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();
    private final UserBO userBO;

    public FraudDetectionService(UserBO userBO) {
        this.userBO = userBO;
    }

    public List<String> getUsername() {
        return fraudDetectionSystem.getUsername();
    }

    public void setUsername(List<String> username) {
        fraudDetectionSystem.setUsername(username);
    }

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

    private double calculateSimilarity(String str1, String str2) {
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
