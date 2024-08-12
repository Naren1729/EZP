/**
 * @Authors: Naren Sri Sai, Arvind
 * @Date : 11/08/2024
 * 
 * @Description:
 * The FraudDetectionServiceTest class is a unit test class designed to validate the functionality 
 * of the FraudDetectionService class. It uses JUnit to ensure that the fraud detection service 
 * processes transactions and calculates risk scores accurately. The test cases include verifying 
 * the fraud detection mechanism and similarity calculations. The setup includes initializing 
 * instances of FraudDetectionService, FraudDetectionBO, and FraudDetectionSystem.
 */


package com.ezp.sac.test;

import static org.junit.Assert.*;

import com.ezp.sac.service.FraudDetectionService;
import com.ezp.sac.repo.FraudDetectionBO;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.model.FraudDetectionSystem;
import org.junit.Before;
import org.junit.Test;

public class FraudDetectionServiceTest {

    private FraudDetectionService fraudDetectionService;
    private FraudDetectionSystem fraudDetectionSystem;
    private UserBO userBO;


    @Before
    public void setUp() throws Exception {
    	// Initialize the UserBO and FraudDetectionService instances before each test
        userBO = UserBO.getInstance();  // Create a UserBO instance
        fraudDetectionService = new FraudDetectionService(userBO);  // Pass the UserBO instance to the service
        new FraudDetectionBO();
        fraudDetectionSystem = new FraudDetectionSystem();
    }


//    @Test
//    public void testGetUsername() {
//        List<String> usernames = Arrays.asList("activity1", "activity2", "activity3");
//        fraudDetectionService.setUsername(usernames);
//        assertEquals(usernames, fraudDetectionService.getUsername());
//    }
//
//    @Test
//    public void testSetUsername() {
//        List<String> usernames = Arrays.asList("activity1", "activity2", "activity3");
//        fraudDetectionService.setUsername(usernames);
//        assertEquals(usernames, fraudDetectionService.getUsername());
//    }

    @Test
    public void testDetectFraud() {
    	// Test to verify that fraud detection system correctly sets the risk score
        fraudDetectionService.detectFraud("johnDoe");
        assertTrue(fraudDetectionSystem.getRiskScore() == 0);  // Assuming "johnDoe" has no similarity, so risk score should be 0
    }

    @Test
    public void testCalculateSimilarity() {
    	// Test to check if the similarity score is calculated correctly
        fraudDetectionService.flagTransaction("activity1");
        assertTrue(fraudDetectionService.calculateSimilarity("activity1", "asdasdasd") < 1);  // Similarity should be less than 1
    }
}
