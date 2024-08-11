package com.ezp.sac.model;


import java.util.List;

/**
 * FraudDetectionSystem class represents a system that detects fraudulent activities.
 * It contains information about usernames and their associated risk scores.
 */
public class FraudDetectionSystem {
	
	// List of usernames to be monitored for fraud detection
	private List<String> username;
	
	// Risk score associated with the usernames
	private double riskScore;
	
	/**
	 * Sets the risk score for the usernames.
	 * 
	 * @param riskScore the risk score to be set
	 */
	public void setRiskScore(double riskScore) {
      this.riskScore = riskScore;
	}

	
	/**
	 * Gets the risk score for the usernames.
	 * @return the risk score
	 */
	public double getRiskScore() {
		return riskScore;
	}

	/**
	 * Gets the list of usernames.
	 * @return the list of usernames
	 */
	 public List<String> getUsername() {
		 return username;
	 }

	 /**
	  * Sets the list of usernames.
	  * @param username the list of usernames to be set
	  */
	 public void setUsername(List<String> username) {
		 this.username = username;
	 }
}
