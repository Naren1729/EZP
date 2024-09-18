/**
 * @author Naren, Bhavansh
 * 
 * @decription:
 * 
 * Entity representing the details of a fraudulent transaction.
 * 
 * This class includes information about the transaction, its associated risk score,
 * and a unique identifier for the fraudulent transaction.
 */

package com.ezpay.entity;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;

/**
 * This class represents the details of a fraudulent transaction.
 */
@Component
@Entity
@Table(name = "fraud_transaction_details")
public class FraudTransactionDetails {

    // Unique identifier for the fraud transaction
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fraud_transaction_details_seq_gen")
    @SequenceGenerator(name = "fraud_transaction_details_seq_gen", sequenceName = "fraud_transaction_details_seq", allocationSize = 1)
    @Column(name = "fraud_id")
    Long fraudID;

    // The associated transaction details
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionDetails transaction;

    // The risk score associated with the transaction
    @Column(name = "risk_score")
    BigDecimal riskScore;

    // Default constructor
    public FraudTransactionDetails() {
        super();
    }

    // Parameterized constructor
    public FraudTransactionDetails(Long fraudID, TransactionDetails transaction, BigDecimal riskScore) {
        this.fraudID = fraudID;
        this.transaction = transaction;
        this.riskScore = riskScore;
    }

    // Getter for fraudID
    public Long getFraudID() {
        return fraudID;
    }

    // Setter for fraudID
    public void setFraudID(Long fraudID) {
        this.fraudID = fraudID;
    }

    // Getter for transaction
    public TransactionDetails getTransaction() {
        return transaction;
    }

    // Setter for transaction
    public void setTransaction(TransactionDetails transaction) {
        this.transaction = transaction;
    }

    // Getter for riskScore
    public BigDecimal getRiskScore() {
        return riskScore;
    }

    // Setter for riskScore
    public void setRiskScore(BigDecimal riskScore) {
        this.riskScore = riskScore;
    }

    // Overriding toString method to provide string representation of the object
    @Override
    public String toString() {
        return "FraudTransactionDetails [fraudID=" + fraudID + ", transaction=" + transaction
                + ", riskScore=" + riskScore + "]";
    }
}
