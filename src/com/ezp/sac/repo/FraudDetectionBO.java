/**
 * @Author : Mayuri, Naren Sri Sai
 * @Date : 19/08/2024
 * 
 * @Description:
 * This class manages the detection of fraudulent activities by storing and retrieving 
 * usernames associated with suspicious behavior. It provides methods to get and set 
 * the list of usernames that are flagged for potential fraud, allowing integration with 
 * other components of the system that monitor and act upon suspicious activities.
 */

package com.ezp.sac.repo;

import java.util.List;
import com.ezp.sac.model.*;

public class FraudDetectionBO {
   
 // Instance of FraudDetectionSystem to manage the risk score and username tracking
    private FraudDetectionSystem fraudDetectionSystem = new FraudDetectionSystem();

    // UserBO is a singleton instance to interact with user data
    private static UserBO userBO;

    // Constructor to initialize the FraudDetectionService with a UserBO instance
    public FraudDetectionBO() {
        userBO = UserBO.getInstance();
    }

    // Gets the list of usernames from the FraudDetectionSystem
    public List<String> getUsername() {
        return fraudDetectionSystem.getUsername();
    }

    // Sets the list of usernames in the FraudDetectionSystem
    public void setUsername(List<String> username) {
        fraudDetectionSystem.setUsername(username);
    }

    /**
     * Detects fraudulent activity by comparing the similarity between the given transaction
     * and the usernames stored in the system. The highest similarity is used to set the risk score.
     * 
     * @param transaction The transaction string to be checked for fraud.
     */
    public void detectFraud(String transaction) {
        // Retrieve all users to check for suspicious activities
        List<User> suspiciousActivities = userBO.getAllUsers();
        double highestSimilarity = 0;

        // Compare the transaction string with each username
        for (User user : suspiciousActivities) {
            String username = user.getUsername();
            double similarity = calculateSimilarity(transaction, username);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
            }
        }

        // Set the risk score based on the highest similarity found
        fraudDetectionSystem.setRiskScore(1 - highestSimilarity);
    }

    /**
     * Flags the transaction as fraudulent if the risk score exceeds a certain threshold.
     * 
     * @param transaction The transaction string to be flagged if risky.
     */
    public void flagTransactionUsername(String transaction) {
        // Detect fraud by calculating the similarity and setting the risk score
        detectFraud(transaction);

        double riskScore = fraudDetectionSystem.getRiskScore();

        // Output the status of the transaction based on the risk score
        if (riskScore > 0.3) {
            System.out.println("Transaction flagged as fraudulent: " + transaction + " with a risk score: " + riskScore);
        } else if (riskScore > 0) {
            System.out.println("The entered username: " + transaction + " is incorrect with a risk score: " + riskScore);
        } else {
            System.out.println("Transaction is safe: " + transaction + " with a risk score: " + riskScore);
        }
    }

    /**
     * Calculates the Jaccard similarity between two strings.
     * 
     * @param str1 The first string.
     * @param str2 The second string.
     * @return The Jaccard similarity coefficient.
     */
    public double calculateSimilarity(String str1, String str2) {
        int intersection = 0;
        int union = str1.length() + str2.length();

        // Calculate the intersection of characters between the two strings
        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) != -1) {
                intersection++;
            }
        }

        // Return the Jaccard similarity coefficient
        return (double) intersection / (union - intersection);
    }

    /**
     * Checks if the provided password matches the user's password in the database.
     * The password can be checked in its original or encrypted form.
     * 
     * @param username   The username of the user.
     * @param password   The password to be checked.
     * @param encryption Indicates if the password should be encrypted before comparison.
     * @return The User object if the password is correct, otherwise null.
     */
    public boolean checkUsername(String username) {
        List<User> users = userBO.getAllUsers();
        for(User user: users) {
        	if(user.getUsername().equals(username)) {
        		return true;
        	}
        }
        return false;
    }
    
    public boolean checkPassword(String username,String password) {
    	User users = userBO.getUserByUsername(username);
    	if(users!=null){
    		if(users.getPassword().equals(password)) {
    			return true;
    		}
    	}
    	return false;
    }
}
