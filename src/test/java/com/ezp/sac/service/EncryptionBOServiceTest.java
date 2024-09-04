/**
 * @author: Mayuri
 * @date: 3/09/2024
 * 
 * @description:
 * Unit tests for the EncryptionBOService class.
 * 
 * This class tests the encryption functionalities of the EncryptionBOService. 
 * It verifies that the encryption methods correctly transform data into its encrypted form.
 * The tests cover various types of data, including User, TransactionDetails, doubles, longs, BigDecimals, and Strings.
 */

package com.ezp.sac.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ezp.sac.Entity.TransactionDetails;
import com.ezp.sac.Entity.User;
import com.ezp.sac.repo.UserRepo;


class EncryptionBOServiceTest {

    @InjectMocks
    private EncryptionBOService encryptionBOService;

    @Mock
    private UserRepo userRepo;

    private AutoCloseable closeable;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Initialization before all tests (if needed)
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Cleanup after all tests (if needed)
    }

    @BeforeEach
    void setUp() throws Exception {
        // Initialize Mockito annotations before each test
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close the Mockito annotations after each test
        closeable.close();
    }

    /**
     * Test encrypting a User object.
     * This test ensures that all fields of the User object are properly encrypted.
     */
    @Test
    void testEncryptUser() {
        // Create a User object with known values
        User user = new User(2L, "johnDoe", "password123", "johndoe@gmail.com",
                             BigDecimal.valueOf(270000.0), false, "naren", new HashSet<>());

        // Encrypt the User object
        User encryptedUser = encryptionBOService.encryptUser(user);

        // Assert that encrypted values are not equal to the original values
        assertNotEquals("johnDoe", encryptedUser.getUsername());
        assertNotEquals("password123", encryptedUser.getPassword());
        assertNotEquals("johndoe@gmail.com", encryptedUser.getEmail());
        assertNotEquals(BigDecimal.valueOf(270000.0), encryptedUser.getCurrentBalance());
        assertNotEquals("naren", encryptedUser.getTransactionPassword());

        // Ensure that encrypted values are not null
        assertNotNull(encryptedUser.getUsername());
        assertNotNull(encryptedUser.getPassword());
        assertNotNull(encryptedUser.getEmail());
        assertNotNull(encryptedUser.getCurrentBalance());
        assertNotNull(encryptedUser.getTransactionPassword());
    }

    /**
     * Test encrypting a TransactionDetails object.
     * This test verifies that all fields of the TransactionDetails object are properly encrypted.
     */
    @Test
    void testEncryptTransaction() {
        // Create a TransactionDetails object with known values
        TransactionDetails transaction = new TransactionDetails(1L, BigDecimal.valueOf(270000.0),
                                                                1L, 2L, "deposit", "completed", null);

        // Encrypt the TransactionDetails object
        TransactionDetails encryptedTransaction = encryptionBOService.encryptTransaction(transaction);

        // Assert that encrypted values are not equal to the original values
        assertNotEquals(BigDecimal.valueOf(270000.0), encryptedTransaction.getAmount());
        assertEquals(1L, encryptedTransaction.getUsernId());
        assertEquals(2L, encryptedTransaction.getDestinationUserId());
        assertNotEquals("deposit", encryptedTransaction.getTransactionType());
        assertNotEquals("completed", encryptedTransaction.getTransactionStatus());

        // Ensure that encrypted values are not null
        assertNotNull(encryptedTransaction.getAmount());
        assertNotNull(encryptedTransaction.getTransactionType());
        assertNotNull(encryptedTransaction.getTransactionStatus());
    }

    /**
     * Test encrypting a Long value.
     * This test verifies that the encryption of a Long value returns an encrypted value different from the original.
     */
    @Test
    void testEncryptLong() {
        Long value = 1L;
        Long encryptedValue = encryptionBOService.encryptLong(value);
        assertNotEquals(value, encryptedValue);
    }

    /**
     * Test encrypting a BigDecimal value.
     * This test ensures that the encryption of a BigDecimal value returns an encrypted value different from the original.
     */
    @Test
    void testEncryptBigDecimal() {
        BigDecimal value = BigDecimal.valueOf(270000.0);
        BigDecimal encryptedValue = encryptionBOService.encryptBigDecimal(value);
        assertNotEquals(value, encryptedValue);
    }

    /**
     * Test encrypting a String value.
     * This test verifies that the encryption of a String value returns an encrypted value different from the original.
     */
    @Test
    void testEncryptString() {
        String value = "johnDoe";
        String encryptedValue = encryptionBOService.encrypt(value);
        assertNotEquals(value, encryptedValue);
    }
}
