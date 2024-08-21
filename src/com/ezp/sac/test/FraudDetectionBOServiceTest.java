/**
 * @Authors: Bhavansh
 * @Date : 11/08/2024
 * 
 * @Description:
 * The FraudDetectionServiceTest class is a unit test class designed to validate the functionality 
 * of the FraudDetectionService class. It uses JUnit and mockito to ensure that the fraud detection service 
 * processes transactions and calculates risk scores accurately. The test cases include verifying 
 * the fraud detection mechanism and similarity calculations. The setup includes initializing 
 * instances of FraudDetectionService, FraudDetectionBO, and FraudDetectionSystem.
 */


package com.ezp.sac.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ezp.sac.repo.FraudDetectionBO;
import com.ezp.sac.service.FraudDetectionBOService;

class FraudDetectionBOServiceTest {

    // Declare the service class and its dependency mock
    private FraudDetectionBOService fraudDetectionBOService;
    private FraudDetectionBO fraudDetectionBOMock;

    // Setup method to initialize the mock and service before each test
    @BeforeEach
    void setUp() throws Exception {
        // Create a mock instance of FraudDetectionBO
        fraudDetectionBOMock = mock(FraudDetectionBO.class);
        // Initialize the service with the mocked dependency
        fraudDetectionBOService = new FraudDetectionBOService(fraudDetectionBOMock);
    }

    // Teardown method to clean up after each test
    @AfterEach
    void tearDown() throws Exception {
        fraudDetectionBOService = null; // Nullify the service reference
        fraudDetectionBOMock = null;    // Nullify the mock reference
    }

    // Test method for checkUsername() functionality
    @Test
    void testCheckUsername() {
        String username1 = "user1";
        String username2 = "user2";
        
        // Mocking the behavior of checkUsername() method for different inputs
        when(fraudDetectionBOMock.checkUsername(username1)).thenReturn(true);
        when(fraudDetectionBOMock.checkUsername(username2)).thenReturn(false);
        
        // Calling the service method and verifying the results
        boolean result1 = fraudDetectionBOService.checkUsername(username1);
        boolean result2 = fraudDetectionBOService.checkUsername(username2);
        
        // Asserting the expected outcomes
        assertTrue(result1, "user1 is mocked so it returns true");
        assertFalse(result2, "user2 is mocked so it returns false");
        
        // Verifying that the mock was interacted with as expected
        verify(fraudDetectionBOMock).checkUsername(username1);
        verify(fraudDetectionBOMock).checkUsername(username2);
    }

    // Test method for checkPassword() functionality
    @Test
    void testCheckPassword() {
    	String username1 = "user1";
        String username2 = "user2";
        String password1 = "secret1";
        String password2 = "secret2";
        
        // Mocking the behavior of checkPassword() method for different inputs
        when(fraudDetectionBOMock.checkPassword(username1,password1)).thenReturn(true);
        when(fraudDetectionBOMock.checkPassword(username2,password2)).thenReturn(false);
        
        // Calling the service method and verifying the results
        boolean result1 = fraudDetectionBOService.checkPassword(username1,password1);
        boolean result2 = fraudDetectionBOService.checkPassword(username2,password2);
        
        // Asserting the expected outcomes
        assertTrue(result1, "secret1 is mocked so it returns true");
        assertFalse(result2, "secret2 is mocked so it returns false");
        
        // Verifying that the mock was interacted with as expected
        verify(fraudDetectionBOMock).checkPassword(username1,password1);
        verify(fraudDetectionBOMock).checkPassword(username2,password2);
    }

    // Test method for calculateSimilarity() functionality
    @Test
    void testCalculateSimilarity() {
        String string1 = "abc";
        String string2 = "abc";
        double expectedSimilarity = 1.0;
        
        String string3 = "def";
        String string4 = "ghi";
        double expectedSimilarity2 = 0.0;

        // Mocking the calculateSimilarity() method for different pairs of strings
        when(fraudDetectionBOMock.calculateSimilarity(string1, string2)).thenReturn(expectedSimilarity);
        when(fraudDetectionBOMock.calculateSimilarity(string3, string4)).thenReturn(expectedSimilarity2);
        
        // Calling the service method and verifying the results
        double similarity = fraudDetectionBOService.calculateSimilarity(string1, string2);
        double similarity2 = fraudDetectionBOService.calculateSimilarity(string3, string4);
        
        // Asserting the expected outcomes
        assertEquals(expectedSimilarity, similarity, "Similarity should be 1.0 for identical strings");
        assertEquals(expectedSimilarity2, similarity2, "Similarity should be 0.0 for non-identical strings");
        
        // Verifying that the mock was interacted with as expected
        verify(fraudDetectionBOMock).calculateSimilarity(string1, string2);
        verify(fraudDetectionBOMock).calculateSimilarity(string3, string4);
    }

    // Test method for detectFraud() functionality
    @Test
    void testDetectFraud() {
        String transaction = "suspiciousTransaction";
        
        // Call the service method
        fraudDetectionBOService.detectFraud(transaction);
        
        // Verify that the mock method was called with the correct argument
        verify(fraudDetectionBOMock).detectFraud(transaction);
    }

    // Test method for flagTransactionUsername() functionality
    @Test
    void testFlagTransactionUsername() {
        String transaction = "fraudulentTransaction";
        
        // Call the service method
        fraudDetectionBOService.flagTransactionUsername(transaction);
        
        // Verify that the mock method was called with the correct argument
        verify(fraudDetectionBOMock).flagTransactionUsername(transaction);
    }
}
