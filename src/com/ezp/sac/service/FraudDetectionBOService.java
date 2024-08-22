/**
 * @Authors: Mayuri, Naren Sri Sai, Arvind
 * @Date : 19/08/2024
 * 
 * @Description:
 * The FraudDetectionBOService class is responsible for detecting and flagging potentially
 * fraudulent transactions based on username similarity analysis. It utilizes a custom
 * Jaccard similarity calculation to compare transaction details with existing usernames
 * in the system, determining a risk score. Depending on the calculated risk score, the 
 * service can flag transactions as fraudulent or incorrect, ensuring enhanced security 
 * and reliability in transaction processing.
 */

package com.ezp.sac.service;

import java.util.List;
import com.ezp.sac.repo.*;
import com.ezp.sac.repo.FraudDetectionBO;

public class FraudDetectionBOService {

	// List to store usernames associated with suspicious activities
    private List<String> username;
    private FraudDetectionBO fraudDetectionBO;
    
    public FraudDetectionBOService(){
    	UserBO.getInstance();
    	fraudDetectionBO = new FraudDetectionBO();
    }
    public FraudDetectionBOService(FraudDetectionBO fraudDetectionBO) {
    	UserBO.getInstance();
		this.fraudDetectionBO = fraudDetectionBO;
    }
    // Getter method to retrieve the list of suspicious usernames
    public List<String> getUsername() {
        return username;
    }

    // Setter method to update the list of suspicious usernames
    public void setUsername(List<String> username) {
        this.username = username;
    }
    
    //Method for checking username
    public boolean checkUsername(String username) {
    	boolean userNameExist =  fraudDetectionBO.checkUsername(username);
    	return userNameExist;
    }
    
  //Method for checking password
    public boolean checkPassword(String username,String password) {
    	boolean passwordExist = fraudDetectionBO.checkPassword(username,password);
    	return passwordExist;
    }
    
  //Method for checking risk score
    public void flagTransactionUsername(String transaction) {
    	fraudDetectionBO.flagTransactionUsername(transaction);
    }
    
    //Method for detecting fraudulent activity by comparing the similarity between the given transaction
    public void detectFraud(String transaction) {
    	fraudDetectionBO.detectFraud(transaction);
    }
    
    //Method for calculating the Jaccard similarity between two strings.
    public double calculateSimilarity(String str1, String str2) {
    	return fraudDetectionBO.calculateSimilarity(str1, str2);
    }
}