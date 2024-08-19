/**
 * @Authors: Mayuri, Naren Sri Sai, Arvind
 * @Date : 19/08/2024
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

import java.util.List;
import com.ezp.sac.repo.*;

public class FraudDetectionService {

	// List to store usernames associated with suspicious activities
    private List<String> username;
    private final UserBO userBO;
    
    public FraudDetectionService(){
    	userBO= UserBO.getInstance();
    }
    // Getter method to retrieve the list of suspicious usernames
    public List<String> getUsername() {
        return username;
    }

    // Setter method to update the list of suspicious usernames
    public void setUsername(List<String> username) {
        this.username = username;
    }
}
