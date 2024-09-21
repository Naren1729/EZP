package com.ezpay.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ezpay.config.AesEncryptor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
@Table(name = "bank_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fraud_detection_seq")
    @SequenceGenerator(name = "fraud_detection_seq", sequenceName = "fraud_detection_seq", allocationSize = 1)
    @Column(name = "id")
    @Convert(converter= AesEncryptor.class)
    Long id;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "username", unique = true)
    String username;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "password")
    String password;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "email")
    String email;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "current_balance", columnDefinition = "CLOB")
    BigDecimal currentBalance;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "is_blockListed")
    boolean isBlockeListed;

    @Convert(converter= AesEncryptor.class)
    @Column(name = "transaction_password")
    String transactionPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<TransactionDetails> transactions;

    public User() {
        super();
    }

    public User(Long id, String username, String password, String email, BigDecimal currentBalance, boolean isBlockeListed,
                String transactionPassword) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentBalance = currentBalance;
        this.isBlockeListed = isBlockeListed;
        this.transactionPassword = transactionPassword;
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
    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, currentBalance, isBlockeListed, transactionPassword, transactions);
    }
    
}
