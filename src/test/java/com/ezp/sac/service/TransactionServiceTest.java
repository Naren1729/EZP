/**
 * @author: Bhavansh
 * @date: 3/09/2024
 * 
 * @desription:
 * Unit tests for the TransactionService class.
 * 
 * This class tests the functionalities of the TransactionService, including:
 * - Retrieving all transactions and fraud transactions
 * - Calculating similarity between two strings
 * - Flagging transactions based on certain criteria
 * - Fetching transaction and fraud transaction details by ID
 * 
 * Mocking is used to simulate interactions with repositories and services.
 */

package com.ezp.sac.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.Transaction;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;
import com.ezpay.repository.FraudRepo;
import com.ezpay.repository.TransactionRepo;
import com.ezpay.repository.UserRepo;
import com.ezpay.service.DecryptionBOService;
import com.ezpay.service.EncryptionBOService;
import com.ezpay.service.TransactionService;
import com.ezpay.service.UserService;


class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private FraudRepo fraudRepo;

    @Mock
    private EncryptionBOService encryptionservice;

    @Mock
    private DecryptionBOService decryptionservice;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations before each test
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test retrieving all transactions.
     * This test verifies that the list of transactions is correctly decrypted and returned.
     */
    @Test
    void testFindAllTransactions() {
        // Arrange
        // Decrypted transaction details
        TransactionDetails decryptedTransaction = new TransactionDetails(
            1L,                            // transactionId
            new BigDecimal("100"),          // amount
            1L,                            // usernId
            2L,                            // destinationUserId
            "deposit",                      // transactionType
            "Pending",                      // transactionStatus
            LocalDateTime.now()             // transactionTime
        );

        // Encrypted transaction details
        TransactionDetails encryptedTransaction = new TransactionDetails(
            1L,                            // transactionId
            new BigDecimal("2940557568"),   // Simulated encrypted value for amount
            1L,                            // usernId
            2L,                            // destinationUserId
            "LyApJDYwPw==",                 // Simulated encrypted value for transactionType
            "GDA6KCAqOA==",                 // Simulated encrypted value for transactionStatus
            LocalDateTime.now()             // transactionTime
        );

        List<TransactionDetails> encryptedTransactions = new ArrayList<>();
        encryptedTransactions.add(encryptedTransaction);

        List<TransactionDetails> decryptedTransactions = new ArrayList<>();
        decryptedTransactions.add(decryptedTransaction);

        // Mock repository and decryption service methods
        when(transactionRepo.findAll()).thenReturn(encryptedTransactions);
        when(decryptionservice.decryptTransaction(encryptedTransaction)).thenReturn(decryptedTransaction);

        // Act
        List<TransactionDetails> result = transactionService.findAllTransactions();

        // Assert
        assertEquals(decryptedTransactions, result);
        verify(transactionRepo).findAll();
        verify(decryptionservice).decryptTransaction(encryptedTransaction);
    }


    /**
     * Test retrieving all fraud transactions.
     * This test verifies that the list of fraud transactions is correctly decrypted and returned.
     */
    @Test
    void testFindAllFraudTransactions() {
        // Arrange
        List<FraudTransactionDetails> encryptedFraudTransactions = new ArrayList<>();
        FraudTransactionDetails encryptedFraudTransaction = new FraudTransactionDetails(
                1L,                            // fraudTransactionId
                new TransactionDetails(
                    1L,                        // transactionId
                    new BigDecimal("2940557568"), // Simulated encrypted value for amount
                    1L,                        // usernId
                    2L,                        // destinationUserId
                    "LyApJDYwPw==",             // Simulated encrypted value for transactionType
                    "GDA6KCAqOA==",             // Simulated encrypted value for transactionStatus
                    LocalDateTime.now()         // transactionTime
                ),
                new BigDecimal("10.0")  // Simulated encrypted value for FraudAmount
            );
        encryptedFraudTransactions.add(encryptedFraudTransaction);

        FraudTransactionDetails decryptedFraudTransaction = new FraudTransactionDetails(1L, new TransactionDetails(1L, new BigDecimal("100.00"), 1L, 2L, "Transfer", "Pending", LocalDateTime.now()), new BigDecimal("10.0"));
        List<FraudTransactionDetails> decryptedFraudTransactions = new ArrayList<>();
        decryptedFraudTransactions.add(decryptedFraudTransaction);

        // Mock repository and decryption service methods
        when(fraudRepo.findAll()).thenReturn(encryptedFraudTransactions);
        when(decryptionservice.decryptFraud(encryptedFraudTransaction)).thenReturn(decryptedFraudTransaction);

        // Act
        List<FraudTransactionDetails> result = transactionService.findAllFraudTransactions();

        // Assert
        assertEquals(decryptedFraudTransactions, result);
        verify(fraudRepo).findAll();
        verify(decryptionservice).decryptFraud(encryptedFraudTransaction);
    }

    /**
     * Test calculating similarity between two strings.
     * This test verifies that the similarity calculation is correct when the strings are identical.
     */
    @Test
    public void testCalculateSimilarity() {
        String str1 = "password";
        String str2 = "password";

        BigDecimal expectedSimilarity = new BigDecimal("1.00");

        // Act
        BigDecimal similarity = transactionService.calculateSimilarity(str1, str2);

        // Assert
        assertEquals(expectedSimilarity, similarity); // Verifying the calculated similarity is as expected
    }

    /**
     * Test flagging a transaction with valid conditions.
     * This test ensures that a transaction is flagged successfully when conditions are met.
     */
    @Test
    void testFlagTransaction() throws Exception {
        // Arrange
        Long userId = 1L;
        Long destinationUserId = 2L;
        BigDecimal validAmount = new BigDecimal("1000");
        String transactionType = "Credit";

        User validUser = new User();
        validUser.setCurrentBalance(new BigDecimal("5000"));
        validUser.setTransactionPassword("password");

        User destinationUser = new User();
        destinationUser.setCurrentBalance(new BigDecimal("3000"));

        Transaction validTransaction = new Transaction();
        validTransaction.setUserID(userId);
        validTransaction.setDestinationUserID(destinationUserId);
        validTransaction.setAmount(validAmount);
        validTransaction.setType(transactionType);
        validTransaction.setTransactionPassword("password");

        TransactionDetails validTransactionDetails = new TransactionDetails();
        validTransactionDetails.setAmount(validAmount);
        validTransactionDetails.setDestinationUserId(destinationUserId);
        validTransactionDetails.setTransactionType(transactionType);
        validTransactionDetails.setUsernId(userId);
        validTransactionDetails.setTransactionTime(LocalDateTime.now());
        validTransactionDetails.setTransactionStatus("Success");

        // Define mocks
        when(userService.findById(userId)).thenReturn(validUser);
        when(userService.findById(destinationUserId)).thenReturn(destinationUser);
        when(encryptionservice.encryptTransaction(any(TransactionDetails.class)))
                .thenReturn(validTransactionDetails);
        when(transactionRepo.save(any(TransactionDetails.class))).thenReturn(validTransactionDetails);

        // Act for valid transaction
        System.out.println("Executing valid transaction...");
        boolean resultValid = transactionService.flagTransaction(validTransaction);

        assertTrue(resultValid);
        assertTrue(resultValid);
        verify(transactionRepo).save(any(TransactionDetails.class));
    }




    /**
     * Test retrieving a transaction by its ID.
     * This test verifies that the transaction details are correctly retrieved and decrypted.
     */
    @Test
    void testGetTransactionById() {
        // Arrange
        Long transactionId = 1L;
        BigDecimal encryptedAmount = new BigDecimal("2940557568"); // Simulated encrypted amount
        String encryptedTransactionType = "LyApJDYwPw=="; // Simulated encrypted transaction type
        String encryptedTransactionStatus = "GDA6KCAqOA=="; // Simulated encrypted transaction status

        // Create a TransactionDetails object with encrypted values
        TransactionDetails encryptedTransactionDetails = new TransactionDetails();
        encryptedTransactionDetails.setTransactionId(transactionId);
        encryptedTransactionDetails.setAmount(encryptedAmount);
        encryptedTransactionDetails.setUsernId(1L);
        encryptedTransactionDetails.setDestinationUserId(2L);
        encryptedTransactionDetails.setTransactionType(encryptedTransactionType);
        encryptedTransactionDetails.setTransactionStatus(encryptedTransactionStatus);
        encryptedTransactionDetails.setTransactionTime(LocalDateTime.now());

        // Create a TransactionDetails object with decrypted values
        TransactionDetails decryptedTransactionDetails = new TransactionDetails();
        decryptedTransactionDetails.setTransactionId(transactionId);
        decryptedTransactionDetails.setAmount(new BigDecimal("1000")); // Actual amount after decryption
        decryptedTransactionDetails.setUsernId(1L);
        decryptedTransactionDetails.setDestinationUserId(2L);
        decryptedTransactionDetails.setTransactionType("Deposit"); // Actual type after decryption
        decryptedTransactionDetails.setTransactionStatus("Success"); // Actual status after decryption
        decryptedTransactionDetails.setTransactionTime(LocalDateTime.now());

        // Mock repository and decryption service methods
        when(transactionRepo.findById(transactionId)).thenReturn(Optional.of(encryptedTransactionDetails));
        when(decryptionservice.decryptTransaction(encryptedTransactionDetails)).thenReturn(decryptedTransactionDetails);

        // Act
        TransactionDetails result = transactionService.getTransactionById(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(decryptedTransactionDetails, result); // Compare the entire object
        verify(transactionRepo).findById(transactionId);
        verify(decryptionservice).decryptTransaction(encryptedTransactionDetails);
    }


    /**
     * Test retrieving fraud transaction details by ID.
     * This test verifies that the fraud transaction details are correctly retrieved and decrypted.
     */
    @Test
    void testGetFraudTransactionDetailsById() {
        // Arrange
        Long fraudTransactionId = 1L;
        BigDecimal encryptedRiskScore = new BigDecimal("2940557568"); // Simulated encrypted risk score
        
        // Create FraudTransactionDetails object with encrypted values
        FraudTransactionDetails encryptedFraudTransactionDetails = new FraudTransactionDetails();
        encryptedFraudTransactionDetails.setTransaction(new TransactionDetails()); // Simulate an encrypted transaction
        encryptedFraudTransactionDetails.setRiskScore(encryptedRiskScore);

        // Create FraudTransactionDetails object with decrypted values
        FraudTransactionDetails decryptedFraudTransactionDetails = new FraudTransactionDetails();
        decryptedFraudTransactionDetails.setTransaction(new TransactionDetails()); // Actual transaction details after decryption
        decryptedFraudTransactionDetails.setRiskScore(new BigDecimal("10.0")); // Actual risk score after decryption

        // Mock repository and decryption service methods
        when(fraudRepo.findById(fraudTransactionId)).thenReturn(Optional.of(encryptedFraudTransactionDetails));
        when(decryptionservice.decryptFraud(encryptedFraudTransactionDetails)).thenReturn(decryptedFraudTransactionDetails);

        // Act
        FraudTransactionDetails result = transactionService.getFraudTransactionDetailsById(fraudTransactionId);

        // Assert
        assertNotNull(result);
        assertEquals(decryptedFraudTransactionDetails.getRiskScore(), result.getRiskScore()); // Compare decrypted risk score
        verify(fraudRepo).findById(fraudTransactionId);
        verify(decryptionservice).decryptFraud(encryptedFraudTransactionDetails);
    }

    
    /**
     * Test detecting fraud in a transaction.
     * This test verifies the fraud detection logic based on user, destination user, and transaction amount.
     */
    @Test
    public void testDetectFraud() {
        // Arrange
        Long userId = 1L;
        Long destinationUserId = 2L;
        LocalDateTime transactionTime = LocalDateTime.of(2024, 9, 2, 3, 0);
        BigDecimal transactionAmount = new BigDecimal("60000");

        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setUsernId(userId);
        transactionDetails.setDestinationUserId(destinationUserId);
        transactionDetails.setTransactionTime(transactionTime);
        transactionDetails.setAmount(transactionAmount);

        User user = new User();
        user.setBlockeListed(false);
        user.setCurrentBalance(new BigDecimal("100000"));

        User destinationUser = new User();
        destinationUser.setBlockeListed(false);

        when(userService.findById(userId)).thenReturn(user);
        when(userService.findById(destinationUserId)).thenReturn(destinationUser);
        when(transactionRepo.findByUsernIdAndDestinationUserIdAndTransactionTimeBetween(anyLong(), anyLong(), any(), any()))
                .thenReturn(Collections.emptyList()); // No previous transactions

        // Simulate the decrypted amount
        when(decryptionservice.decryptBigDecimal(any(BigDecimal.class))).thenReturn(transactionAmount);

        // Act
        BigDecimal riskScore = transactionService.detectFraud(transactionDetails);

        // Assert
        assertEquals(new BigDecimal("25"), riskScore); 
    }
}
