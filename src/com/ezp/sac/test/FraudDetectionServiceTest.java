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
    	User user = fraudDetectionService.checkPassword("johnDoe","password123",false);
    	System.out.println(user);
    	assertEquals("User [username=johnDoe, name=John Doe, password=password123, transaction_id=1001, type=deposit, amount=250.75, date=12-08-2024 23:22:01, status=completed]".substring(0, 90),user.toString().substring(0, 90));
    }
//    public void checkPasswordSuite() {
//    	User user = fraudDetectionService.checkPassword("johnDoe","password123",true);
//    	System.out.println(user);
//    	assertEquals("User [username=ISoxJQE2Lg==, name=ASoxJWUdJCA=, password=OyQqODI2OSFoeXY=, transaction_id=15607355648, type=LyApJDYwPw==, amount=6.218472233E-315, date=13-08-2024 16:46:30, status=KCo0Oyk8PyA9]".substring(0, 90),user.toString().substring(0, 90));
//    }
}