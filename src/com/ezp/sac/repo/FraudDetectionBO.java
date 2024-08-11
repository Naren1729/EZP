package com.ezp.sac.repo;

import java.util.List;
import java.util.ArrayList;

public class FraudDetectionBO {

    // List to store usernames associated with suspicious activities
    private List<String> username;

    // Constructor to initialize the list and add hardcoded suspicious activities
    public FraudDetectionBO() {
        username = new ArrayList<>();
        
        // Hardcoded suspicious activities (usernames)
        username.add("activity1");
        username.add("activity2");
        username.add("activity3");
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
