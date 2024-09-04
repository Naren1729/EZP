/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * */

package com.ezp.sac.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezp.sac.Entity.FraudTransactionDetails;
import com.ezp.sac.Entity.Transaction;
import com.ezp.sac.Entity.TransactionDetails;
import com.ezp.sac.Entity.User;
import com.ezp.sac.exception.EncryptionOrDecryptionException;
import com.ezp.sac.exception.UserNotFoundException;
import com.ezp.sac.repo.FraudRepo;
import com.ezp.sac.repo.TransactionRepo;
import com.ezp.sac.repo.UserRepo;

@Service
public class TransactionService implements TransactionInterface {
    
    @Autowired
    private UserService userService; // Service to handle user-related operations
    
    @Autowired
    private TransactionRepo transactionRepo; // Repository for transaction details
    
    @Autowired
    private UserRepo userRepo; // Repository for user details
    
    @Autowired
    private FraudRepo fraudRepo; // Repository for fraud transaction details
    
    @Autowired
    private EncryptionBOService encryptionservice; // Service for encrypting data
    
    @Autowired
    private DecryptionBOService decryptionservice; // Service for decrypting data
    
    /**
     * Retrieves a transaction by its ID and decrypts the details.
     * @param Transaction_Id The ID of the transaction.
     * @return Decrypted transaction details.
     * @throws UserNotFoundException if no transaction is found with the given ID.
     * @throws EncryptionOrDecryptionException if decryption fails.
     */
    public TransactionDetails getTransactionById(Long Transaction_Id) {
        // Fetch transaction details by ID
        TransactionDetails transactionDetails = transactionRepo.findById(Transaction_Id)
                .orElseThrow(() -> new UserNotFoundException("Id Invalid"));

        // Decrypt transaction details
        TransactionDetails decryptedTransactionDetails = decryptionservice.decryptTransaction(transactionDetails);    
        return decryptedTransactionDetails;
    }
    
    /**
     * Retrieves fraud transaction details by its ID and decrypts the details.
     * @param id The ID of the fraud transaction.
     * @return Decrypted fraud transaction details.
     * @throws UserNotFoundException if no fraud transaction is found with the given ID.
     * @throws EncryptionOrDecryptionException if decryption fails.
     */
    public FraudTransactionDetails getFraudTransactionDetailsById(Long id) {
        // Fetch fraud transaction details by ID
        FraudTransactionDetails fraudTransactionDetails = fraudRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Id Invalid"));

        // Decrypt fraud transaction details
        FraudTransactionDetails decryptedFraudTransactionDetails = decryptionservice.decryptFraud(fraudTransactionDetails);    
        return decryptedFraudTransactionDetails;
    }
    
    /**
     * Flags a transaction based on various conditions and updates the transaction status.
     * @param transaction The transaction to be flagged.
     * @return true if the transaction is successful, false otherwise.
     */
    public boolean flagTransaction(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        Long userId = transaction.getUserID();
        Long destinationUserId = transaction.getDestinationUserID();
        String transactionType = transaction.getType();
        String status = "";
        boolean pass = false;
        boolean blackList = false;

        // Create a new TransactionDetails object
        TransactionDetails transactionDetails1 = new TransactionDetails();
        transactionDetails1.setAmount(amount);
        transactionDetails1.setDestinationUserId(destinationUserId);
        transactionDetails1.setTransactionType(transactionType);
        transactionDetails1.setUsernId(userId);
        transactionDetails1.setTransactionTime(LocalDateTime.now());
        
        // Retrieve user and destination user details
        User user = userService.findById(userId);
        User destinationUser = userService.findById(destinationUserId);
        
        // Check if users are blacklisted
        boolean isBlackListeduser = user.getIsBlockeListed();
        boolean isBlackListedDestinationUser = destinationUser.getIsBlockeListed();
        
        blackList = !isBlackListeduser && !isBlackListedDestinationUser;
        
        // Check transaction password validity
        String transactionPassword = transaction.getTransactionPassword();
        String userTransactionPassword = user.getTransactionPassword();
        pass = transactionPassword.equals(userTransactionPassword);
        
        // Get current balance of the user
        BigDecimal userCurrentBalance = user.getCurrentBalance(); 

        // Check various conditions for the transaction
        if(amount.compareTo(userCurrentBalance) > 0 || amount.compareTo(BigDecimal.ZERO) < 0 
                || amount.compareTo(new BigDecimal("50000")) > 0 || !pass || !blackList 
                || userCurrentBalance.compareTo(BigDecimal.ZERO) < 0) {
            status = "failed";
            transactionDetails1.setTransactionStatus(status);
            TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
            transactionRepo.save(encryptedTransaction);

            // Fraud detection and risk score calculation
            double riskScore = 0.0;
            if(amount.compareTo(new BigDecimal("50000")) > 0) {
                riskScore += 25;
            }
            if(amount.compareTo(user.getCurrentBalance()) > 0) {
                riskScore += 25;
            }
            if(!pass) {
                riskScore += (1 - calculateSimilarity(transactionPassword, userTransactionPassword).doubleValue()) * 100;
            }
            if(!blackList) {
                riskScore = 100.0;
            }
            
            // Save fraud transaction details
            FraudTransactionDetails fraudTransactionDetails = new FraudTransactionDetails();
            fraudTransactionDetails.setRiskScore(riskScore);
            fraudTransactionDetails.setTransaction(transactionDetails1);
            fraudRepo.save(fraudTransactionDetails);    
            
            // Encrypt and save user details
            User encryptUser = encryptionservice.encryptUser(user);
            User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
            userRepo.save(encryptUser);
            userRepo.save(encryptDestinationUser);
            
            return false;
        } else {
            status = "Success";
            transactionDetails1.setTransactionStatus(status);
            TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
            transactionRepo.save(encryptedTransaction);

            // Update user balances
            user.setCurrentBalance(user.getCurrentBalance().subtract(amount));
            destinationUser.setCurrentBalance(destinationUser.getCurrentBalance().add(amount));
            
            // Encrypt and save updated user details
            User encryptUser = encryptionservice.encryptUser(user);
            User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
            userRepo.save(encryptUser);
            userRepo.save(encryptDestinationUser);
        }
        
        return true;
    }
    
    /**
     * Retrieves and decrypts all transaction details from the repository.
     * @return List of decrypted transaction details.
     */
    public List<TransactionDetails> findAllTransactions(){
        List<TransactionDetails> encryptedTransactions = transactionRepo.findAll();
        List<TransactionDetails> decryptedTransactions = new ArrayList<>();
        for(TransactionDetails transaction : encryptedTransactions) {
            decryptedTransactions.add(decryptionservice.decryptTransaction(transaction));
        }
        return decryptedTransactions;
    }
    
    /**
     * Retrieves and decrypts all fraud transaction details from the repository.
     * @return List of decrypted fraud transaction details.
     */
    public List<FraudTransactionDetails> findAllFraudTransactions(){
        List<FraudTransactionDetails> encryptedFraudTransactions = fraudRepo.findAll();
        List<FraudTransactionDetails> decryptedFraudTransactions = new ArrayList<>();
        for(FraudTransactionDetails fraudTransaction : encryptedFraudTransactions) {
            decryptedFraudTransactions.add(decryptionservice.decryptFraud(fraudTransaction));
        }
        return decryptedFraudTransactions;
    }
    
    /**
     * Calculates the similarity between two strings.
     * @param str1 First string.
     * @param str2 Second string.
     * @return Similarity score as a BigDecimal.
     */
    public BigDecimal calculateSimilarity(String str1, String str2) {
        int intersection = 0;
        int union = str1.length() + str2.length();

        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) != -1) {
                intersection++;
            }
        }

        return new BigDecimal(intersection).divide(new BigDecimal(union - intersection), BigDecimal.ROUND_HALF_UP);
    }
}
