/**
 * @Author : Naren Sri Sai
 * @Date : 11/08/2024
 * 
 * @Description:
 * This class manages the detection of fraudulent activities by storing and retrieving 
 * usernames associated with suspicious behavior. It provides methods to get and set 
 * the list of usernames that are flagged for potential fraud, allowing integration with 
 * other components of the system that monitor and act upon suspicious activities.
 */

package com.ezp.sac.repo;

import java.util.List;

public class FraudDetectionBO {

    // List to store usernames associated with suspicious activities
    private List<String> username;


    // Getter method to retrieve the list of suspicious usernames
    public List<String> getUsername() {
        return username;
    }

    // Setter method to update the list of suspicious usernames
    public void setUsername(List<String> username) {
        this.username = username;
    }
}
