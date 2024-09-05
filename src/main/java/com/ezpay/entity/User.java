/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * @description:
 * The User class represents a bank user with details such as username, password, email, current balance, and transaction history.
 */

package com.ezpay.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Component
@Entity
@Table(name = "bank_user")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fraud_detection_seq")
    @SequenceGenerator(name = "fraud_detection_seq", sequenceName = "fraud_detection_seq", allocationSize = 1)
    @Column(name = "id")
    Long id;

    /**
     * The username of the user.
     */
    @Column(name = "username")
    String username;

    /**
     * The password of the user.
     */
    @Column(name = "password")
    String password;

    /**
     * The email address of the user.
     */
    @Column(name = "email")
    String email;

    /**
     * The current balance of the user's account.
     */
    @Column(name = "current_balance")
    BigDecimal currentBalance;

    /**
     * Indicates whether the user is blocklisted.
     */
    @Column(name = "is_blockListed")
    boolean isBlockeListed;

    /**
     * The transaction password of the user.
     */
    @Column(name = "transaction_password")
    String transactionPassword;

    /**
     * The set of transactions associated with the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TransactionDetails> transactions;

    /**
     * Default constructor.
     */
    public User() {
        super();
    }

    /**
     * Parameterized constructor to initialize user details.
     *
     * @param id                   the user ID
     * @param username             the username
     * @param password             the password
     * @param email                the email address
     * @param currentBalance       the current balance
     * @param isBlockeListed       indicates if the user is blocklisted
     * @param transactionPassword  the transaction password
     * @param transactions         the set of transactions
     */
    public User(Long id, String username, String password, String email, BigDecimal currentBalance, boolean isBlockeListed,
                String transactionPassword, Set<TransactionDetails> transactions) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentBalance = currentBalance;
        this.isBlockeListed = isBlockeListed;
        this.transactionPassword = transactionPassword;
        this.transactions = transactions;
    }

    // Getters and setters for the fields

    public boolean getIsBlockeListed() {
        return isBlockeListed;
    }

    public void setBlockeListed(boolean isBlockeListed) {
        this.isBlockeListed = isBlockeListed;
    }

    public String getTransactionPassword() {
        return transactionPassword;
    }

    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Set<TransactionDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDetails> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
                + ", currentBalance=" + currentBalance + ", isBlockeListed=" + isBlockeListed + ", transactionPassword="
                + transactionPassword + ", transactions=" + transactions + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                currentBalance.equals(user.currentBalance) &&
                isBlockeListed == user.isBlockeListed &&
                transactionPassword.equals(user.transactionPassword) &&
                transactions.equals(user.transactions);
    }
}
