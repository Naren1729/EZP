/**
 * @author: Mayuri
 * @date: 3/09/2024
 * 
 * @description:
 * Unit tests for the DecryptionBOService class.
 * 
 * This class tests the decryption functionalities of the DecryptionBOService. 
 * It verifies that the decryption methods correctly transform encrypted data back into its original form.
 * The tests cover various types of data, including User, TransactionDetails, FraudTransactionDetails,
 * Strings, BigDecimals, doubles, and Longs.
 */

package com.ezp.sac.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;
import com.ezpay.repository.UserRepo;
import com.ezpay.service.DecryptionBOService;


@ExtendWith(MockitoExtension.class)
class DecryptionBOServiceTest {

    @InjectMocks
    private DecryptionBOService decryptionBOService;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test decrypting a User object.
     * This test checks that the decryption of a User object transforms all encrypted fields back 
     * to their original values.
     */
//    @Test
//    void testDecryptUser() {
//        // Create an encrypted User object
//        User encryptedUser = new User();
//        encryptedUser.setUsername("CS04PSQ3OC0=");
//        encryptedUser.setPassword("OyQqODI2OSFoeXY=");
//        encryptedUser.setEmail("ISoxJSE2LgUeJiQwJ2s6JCg=");
//        BigInteger bigIntValue = new BigInteger("169034995968");
//        encryptedUser.setCurrentBalance(new BigDecimal(bigIntValue));
//        encryptedUser.setTransactionPassword("JSQrLis=");
//
//        // Decrypt the User object
//        User decryptedUser = decryptionBOService.decryptUser(encryptedUser);
//
//        // Assert that the decrypted values match expected values
//        assertEquals("Bhavansh", decryptedUser.getUsername());
//        assertEquals("password123", decryptedUser.getPassword());
//        assertEquals("johndoe@Gmail.com", decryptedUser.getEmail());
//        assertEquals(new BigDecimal("10000"), decryptedUser.getCurrentBalance());
//        assertEquals("naren", decryptedUser.getTransactionPassword());
//    }

    /**
     * Test decrypting a TransactionDetails object.
     * This test verifies that the decryption of a TransactionDetails object accurately converts 
     * encrypted fields back to their original values.
     */
    @Test
    void testDecryptTransaction() {
        // Create an encrypted TransactionDetails object
        TransactionDetails encryptedTransaction = new TransactionDetails();
        encryptedTransaction.setAmount(new BigDecimal("4.529759855232E+13"));
        encryptedTransaction.setUsernId(1L);
        encryptedTransaction.setDestinationUserId(2L);
        encryptedTransaction.setTransactionType("ISoxJQE2Lg==");
        encryptedTransaction.setTransactionStatus("OyQqODI2OSFoeXY=");

        // Decrypt the TransactionDetails object
        TransactionDetails decryptedTransaction = decryptionBOService.decryptTransaction(encryptedTransaction);

        // Assert that the decrypted values match expected values
        assertEquals(new BigDecimal("2.69919E+6"), decryptedTransaction.getAmount());
        assertEquals(1L, decryptedTransaction.getUsernId());
        assertEquals(2L, decryptedTransaction.getDestinationUserId());
    }

    /**
     * Test decrypting a FraudTransactionDetails object.
     * This test checks that the decryption of a FraudTransactionDetails object properly returns 
     * the decrypted TransactionDetails and risk score.
     */
    @Test
    void testDecryptFraudTransaction() {
        // Create an encrypted FraudTransactionDetails object
        FraudTransactionDetails encryptedFraudTransaction = new FraudTransactionDetails();
        TransactionDetails encryptedTransaction = new TransactionDetails();
        encryptedTransaction.setAmount(new BigDecimal("4.529759855232E+13"));
        encryptedTransaction.setUsernId(1L);
        encryptedTransaction.setDestinationUserId(2L);
        encryptedTransaction.setTransactionType("ISoxJQE2Lg==");
        encryptedTransaction.setTransactionStatus("OyQqODI2OSFoeXY=");
        encryptedFraudTransaction.setTransaction(encryptedTransaction);
        encryptedFraudTransaction.setRiskScore(new BigDecimal("123456789.52"));

        // Decrypt the FraudTransactionDetails object
        FraudTransactionDetails decryptedFraudTransaction = decryptionBOService.decryptFraud(encryptedFraudTransaction);
        System.out.println(decryptedFraudTransaction);

        // Assert that the decrypted values match expected values
        assertEquals(new BigDecimal("6.60"), decryptedFraudTransaction.getRiskScore());
        assertEquals(new BigDecimal("2.69919E+6"), decryptedFraudTransaction.getTransaction().getAmount());
        assertEquals(1L, decryptedFraudTransaction.getTransaction().getUsernId());
        assertEquals(2L, decryptedFraudTransaction.getTransaction().getDestinationUserId());
    }

    /**
     * Test decrypting a String.
     * This test ensures that a decrypted string matches the expected plaintext value.
     */
    @Test
    void testDecryptString() {
        // Encrypted String: "ISoxJQE2Lg==" corresponds to "johnDoe"
        String encryptedString = "ISoxJQE2Lg==";
        String expectedDecryptedString = "johnDoe";

        // Perform Decryption
        String decryptedString = decryptionBOService.decrypt(encryptedString);

        // Assert that the decrypted string matches the expected value
        assertEquals(expectedDecryptedString, decryptedString, "Decrypted string should match the expected value");
    }

    /**
     * Test decrypting a BigDecimal.
     * This test checks that the decryption of a BigDecimal field returns the expected value.
     */
    @Test
    void testDecryptBigDecimal() {
        // Encrypted BigDecimal: 4.529759855232E+13 corresponds to 270000.0
        BigDecimal encryptedBigDecimal = new BigDecimal("451831748864");
        BigDecimal expectedDecryptedBigDecimal = new BigDecimal("26856");

        // Perform Decryption
        BigDecimal decryptedBigDecimal = decryptionBOService.decryptBigDecimal(encryptedBigDecimal);

        // Assert that the decrypted BigDecimal matches the expected value
        assertEquals(expectedDecryptedBigDecimal, decryptedBigDecimal, "Decrypted BigDecimal should match the expected value");
    }

    /**
     * Test decrypting a Long.
     * This test ensures that decrypting a Long value returns the original encrypted value.
     */
    @Test
    void testDecryptLong() {
        // Example encrypted Long: Let's assume 123456789L encrypts to a value, but since encryption logic is symmetric,
        // decrypting it back should return the original value.
        Long encryptedLong = 123456789L;
        Long expectedDecryptedLong = 2203409207435198540L;

        // Perform Decryption
        Long decryptedLong = decryptionBOService.decryptLong(encryptedLong);
        System.out.println(decryptedLong);

        // Assert that the decrypted Long matches the expected value
        assertEquals(expectedDecryptedLong, decryptedLong, "Decrypted Long should match the expected value");
    }
}
