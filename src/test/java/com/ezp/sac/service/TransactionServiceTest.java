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
import com.ezpay.repo.FraudRepo;
import com.ezpay.repo.TransactionRepo;
import com.ezpay.repo.UserRepo;
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
        List<TransactionDetails> encryptedTransactions = new ArrayList<>();
        TransactionDetails encryptedTransaction = new TransactionDetails(1L, new BigDecimal("100.00"), 1L, 2L, "Transfer", "Pending", LocalDateTime.now());
        encryptedTransactions.add(encryptedTransaction);

        TransactionDetails decryptedTransaction = new TransactionDetails(1L, new BigDecimal("100.00"), 1L, 2L, "Transfer", "Pending", LocalDateTime.now());
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
        FraudTransactionDetails encryptedFraudTransaction = new FraudTransactionDetails(1L, new TransactionDetails(1L, new BigDecimal("100.00"), 1L, 2L, "Transfer", "Pending", LocalDateTime.now()), new BigDecimal("10.0"));
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
    void testFlagTransaction_Success() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("1000"));
        transaction.setUserID(1L);
        transaction.setDestinationUserID(2L);
        transaction.setType("transfer");
        transaction.setTransactionPassword("password123");

        User user = new User();
        user.setTransactionPassword("password123");
        user.setCurrentBalance(new BigDecimal("10000"));
        user.setBlockeListed(false);

        User destinationUser = new User();
        destinationUser.setCurrentBalance(new BigDecimal("5000"));
        destinationUser.setBlockeListed(false);

        // Mock service and encryption methods
        when(userService.findById(transaction.getUserID())).thenReturn(user);
        when(userService.findById(transaction.getDestinationUserID())).thenReturn(destinationUser);
        when(encryptionservice.encryptTransaction(any(TransactionDetails.class))).thenReturn(new TransactionDetails());
        when(encryptionservice.encryptUser(any(User.class))).thenReturn(new User());

        // Act
        boolean result = transactionService.flagTransaction(transaction);

        // Assert
        assertTrue(result);
    }

    /**
     * Test flagging a transaction with invalid conditions.
     * This test ensures that a transaction is not flagged when conditions are not met.
     */
    @Test
    void testFlagTransaction_Failure() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("60000"));
        transaction.setUserID(1L);
        transaction.setDestinationUserID(2L);
        transaction.setType("transfer");
        transaction.setTransactionPassword("password123");

        User user = new User();
        user.setTransactionPassword("password123");
        user.setCurrentBalance(new BigDecimal("10000"));
        user.setBlockeListed(false);

        User destinationUser = new User();
        destinationUser.setCurrentBalance(new BigDecimal("5000"));
        destinationUser.setBlockeListed(false);

        // Mock service and encryption methods
        when(userService.findById(transaction.getUserID())).thenReturn(user);
        when(userService.findById(transaction.getDestinationUserID())).thenReturn(destinationUser);
        when(encryptionservice.encryptTransaction(any(TransactionDetails.class))).thenReturn(new TransactionDetails());
        when(encryptionservice.encryptUser(any(User.class))).thenReturn(new User());

        // Act
        boolean result = transactionService.flagTransaction(transaction);

        // Assert
        assertFalse(result);
    }

    /**
     * Test retrieving a transaction by its ID.
     * This test verifies that the transaction details are correctly retrieved and decrypted.
     */
    @Test
    void testGetTransactionById() {
        // Arrange
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionId(1L);
        transactionDetails.setAmount(new BigDecimal("1000"));
        transactionDetails.setUsernId(1L);
        transactionDetails.setDestinationUserId(2L);
        transactionDetails.setTransactionType("Deposit");
        transactionDetails.setTransactionStatus("Success");
        transactionDetails.setTransactionTime(LocalDateTime.now());

        // Mock repository and decryption methods
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(transactionDetails));
        when(decryptionservice.decryptTransaction(any(TransactionDetails.class))).thenReturn(transactionDetails);

        // Act
        TransactionDetails result = transactionService.getTransactionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(transactionDetails, result); // Compare the entire object
    }

    /**
     * Test retrieving fraud transaction details by ID.
     * This test verifies that the fraud transaction details are correctly retrieved and decrypted.
     */
    @Test
    void testGetFraudTransactionDetailsById() {
        // Arrange
        FraudTransactionDetails fraudTransactionDetails = new FraudTransactionDetails();
        fraudTransactionDetails.setTransaction(new TransactionDetails());
        fraudTransactionDetails.setRiskScore(new BigDecimal("10.0"));

        // Mock repository and decryption methods
        when(fraudRepo.findById(1L)).thenReturn(Optional.of(fraudTransactionDetails));
        when(decryptionservice.decryptFraud(any(FraudTransactionDetails.class))).thenReturn(fraudTransactionDetails);

        // Act
        FraudTransactionDetails result = transactionService.getFraudTransactionDetailsById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("10.0"), result.getRiskScore());
    }
}
