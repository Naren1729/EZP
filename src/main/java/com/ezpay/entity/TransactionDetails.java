/**
 * @author: Naren, Bhavansh
 * @date:   2/09/2024
 * 
 * @description:
 * The TransactionDetails class represents the details of a transaction.
 * It includes information such as transaction ID, amount, user IDs, transaction type, status, and time.
 */

package com.ezpay.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Component
@Entity
@Table(name = "transaction_details")
public class TransactionDetails {

    /**
     * The unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_details_seq_gen")
    @SequenceGenerator(name = "transaction_details_seq_gen", sequenceName = "transaction_details_seq", allocationSize = 1)
    @Column(name = "transaction_id")
    Long transactionId;

    /**
     * The amount involved in the transaction.
     */
    @Column(name = "amount")
    BigDecimal amount;

    /**
     * The ID of the user initiating the transaction.
     */
    @Column(name = "user_id")
    Long usernId;

    /**
     * The ID of the user receiving the transaction.
     */
    @Column(name = "destination_user_id")
    Long destinationUserId;

    /**
     * The type of the transaction (e.g., credit, debit).
     */
    @Column(name = "transaction_type")
    String transactionType;

    /**
     * The status of the transaction (e.g., pending, completed).
     */
    @Column(name = "transaction_status")
    String transactionStatus;

    /**
     * The time when the transaction occurred.
     */
    @Column(name = "transaction_time")
    LocalDateTime transactionTime;

    /**
     * The user associated with the transaction.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    /**
     * The details of any fraudulent activity associated with the transaction.
     */
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", insertable = false, updatable = false)
    private FraudTransactionDetails fraudTransactionDetails;

    /**
     * Default constructor.
     */
    public TransactionDetails() {
        super();
    }

    /**
     * Parameterized constructor to initialize transaction details.
     *
     * @param transactionId       the transaction ID
     * @param amount              the transaction amount
     * @param usernId             the user ID initiating the transaction
     * @param destinationUserId   the user ID receiving the transaction
     * @param transactionType     the type of transaction
     * @param transactionStatus   the status of the transaction
     * @param transactionTime     the time of the transaction
     */
    public TransactionDetails(Long transactionId, BigDecimal amount, Long usernId, Long destinationUserId,
                              String transactionType, String transactionStatus, LocalDateTime transactionTime) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.usernId = usernId;
        this.destinationUserId = destinationUserId;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.transactionTime = transactionTime;
    }

    // Getters and setters for the fields

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUsernId() {
        return usernId;
    }

    public void setUsernId(Long usernId) {
        this.usernId = usernId;
    }

    public Long getDestinationUserId() {
        return destinationUserId;
    }

    public void setDestinationUserId(Long destinationUserId) {
        this.destinationUserId = destinationUserId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FraudTransactionDetails getFraudTransactionDetails() {
        return fraudTransactionDetails;
    }

    public void setFraudTransactionDetails(FraudTransactionDetails fraudTransactionDetails) {
        this.fraudTransactionDetails = fraudTransactionDetails;
    }

    @Override
    public String toString() {
        return "TransactionDetails [transactionId=" + transactionId + ", amount=" + amount + ", usernId=" + usernId
                + ", destinationUserId=" + destinationUserId + ", transactionType=" + transactionType
                + ", transactionStatus=" + transactionStatus + ", transactionTime=" + transactionTime + "]";
    }
}
