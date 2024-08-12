//Authors: Naren Sri Sai, Arvind
package com.ezp.sac.test;

import static org.junit.Assert.*;
import com.ezp.sac.model.*;

import com.ezp.sac.service.FraudDetectionService;
import com.ezp.sac.repo.FraudDetectionBO;
import com.ezp.sac.repo.UserBO;
import com.ezp.sac.model.FraudDetectionSystem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FraudDetectionServiceTest {

    private FraudDetectionService fraudDetectionService;
    private FraudDetectionBO fakeDatabase;
    private FraudDetectionSystem fraudDetectionSystem;
    private UserBO userBO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        userBO = UserBO.getInstance();  // Create a UserBO instance
        fraudDetectionService = new FraudDetectionService(userBO);  // Pass the UserBO instance to the service
        fakeDatabase = new FraudDetectionBO();
        fraudDetectionSystem = new FraudDetectionSystem();
    }

    @After
    public void tearDown() throws Exception {
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
        fraudDetectionService.detectFraud("johnDoe");
        assertTrue(fraudDetectionSystem.getRiskScore() == 0);
    }

    @Test
    public void testCalculateSimilarity() {
        fraudDetectionService.flagTransaction("activity1");
        assertTrue(fraudDetectionService.calculateSimilarity("activity1", "asdasdasd") < 1);
    }
    
    @Test
    public void checkPassword() {
    	User user = fraudDetectionService.checkPassword("password123");
    	assertEquals("User [username=johnDoe, name=John Doe, password=password123, transaction_id=1001, type=deposit, amount=250.75, date=12-08-2024 23:22:01, status=completed]".substring(0, 90),user.toString().substring(0, 90));
    }
}