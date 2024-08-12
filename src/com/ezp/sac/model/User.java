/**
 * @Author : Bhavansh, Arvind
 * @Date : 11/08/2024
 * 
 * @Description:
 * This class represents a User model that contains user information as well as transaction details.
 * It includes fields for storing user credentials and transaction attributes such as transaction ID, type,
 * amount, date, and status. The class provides getters and setters for these fields, a constructor for initialization,
 * and a toString method for displaying user and transaction details in a formatted string.
 */

package com.ezp.sac.model;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class User {
    // User-related fields
    public String username;  // Username of the user
    public String name;      // Full name of the user
    public String password;  // User's password

    // Transaction-related fields
    public Long transaction_id;  // Unique identifier for the transaction
    public String type;          // Type of the transaction (e.g., payment, refund)
    public Double amount;        // Transaction amount
    public LocalDateTime date;   // Date and time of the transaction
    public String status;        // Status of the transaction (e.g., completed, pending)

    // Getter and Setter methods for each field
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Constructor for initializing all fields
    public User(String username, String name, String password, Long transaction_id, String type, Double amount,
            LocalDateTime date, String status) {
        super();
        this.username = username;
        this.name = name;
        this.password = password;
        this.transaction_id = transaction_id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Default constructor
    public User() {}

    // Overriding the toString method to provide a string representation of the User object
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "User [username=" + username + ", name=" + name + ", password=" + password + ", transaction_id="
                + transaction_id + ", type=" + type + ", amount=" + amount + ", date=" + date.format(formatter) + ", status=" + status
                + "]";
    }
}
