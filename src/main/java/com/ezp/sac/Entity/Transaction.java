/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * @description:
 * Represents a financial transaction between users.
 * 
 * This class contains details about the transaction amount, the users involved 
 * (initiating and receiving), the type of transaction (e.g., transfer, payment),
 * and the transaction password used for authorization.
 */


package com.ezp.sac.Entity;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

 
@Component
public class Transaction {
    // The amount of the transaction
    BigDecimal amount;
    
    // The ID of the user initiating the transaction
    Long userID;
    
    // The ID of the user receiving the transaction
    Long destinationUserID;
    
    // The type of the transaction (e.g., transfer, payment)
    String type;
    
    // The password for authorizing the transaction
    String transactionPassword;
    
    
    // Gets the transaction password.@return the transaction password
     
    public String getTransactionPassword() {
        return transactionPassword;
    }
    
    
     // Sets the transaction password. @param transactionPassword the transaction password to set
    
    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }
    
    
     //Default constructor for the Transaction class.
    
    public Transaction() {
        super();
    }
    
   
     //Gets the amount of the transaction. @return the amount of the transaction
     
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Sets the amount of the transaction.
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    /**
     * Gets the ID of the user initiating the transaction.
     * @return the user ID
     */
    public Long getUserID() {
        return userID;
    }
    
    /**
     * Sets the ID of the user initiating the transaction.
     * @param userID the user ID to set
     */
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    
    /**
     * Gets the ID of the user receiving the transaction.
     * @return the destination user ID
     */
    public Long getDestinationUserID() {
        return destinationUserID;
    }
    
    /**
     * Sets the ID of the user receiving the transaction.
     * @param destinationUserID the destination user ID to set
     */
    public void setDestinationUserID(Long destinationUserID) {
        this.destinationUserID = destinationUserID;
    }
    
    /**
     * Gets the type of the transaction.
     * @return the type of the transaction
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the type of the transaction.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Constructor with parameters for the Transaction class.
     * @param amount the amount of the transaction
     * @param userID the ID of the user initiating the transaction
     * @param destinationUserID the ID of the user receiving the transaction
     * @param type the type of the transaction
     */
    public Transaction(BigDecimal amount, Long userID, Long destinationUserID, String type) {
        super();
        this.amount = amount;
        this.userID = userID;
        this.destinationUserID = destinationUserID;
        this.type = type;
    }
    
    /**
     * Returns a string representation of the Transaction object.
     * @return a string representation of the Transaction object
     */
    @Override
    public String toString() {
        return "Transaction [amount=" + amount + ", userID=" + userID + ", destinationUserID=" + destinationUserID
                + ", type=" + type + "]";
    }
}
