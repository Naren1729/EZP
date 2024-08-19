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
import com.ezp.sac.service.FraudDetectionBOService;
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

    private FraudDetectionBOService fraudDetectionBOService;
    private FraudDetectionSystem fraudDetectionSystem;
    private UserBO userBO;

    @Before
    public void setUp() throws Exception {
        userBO = UserBO.getInstance();  // Create a UserBO instance
        fraudDetectionBOService = new FraudDetectionBOService();  // Pass the UserBO instance to the service
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
    	fraudDetectionBOService.detectFraud("johnDoe");
        assertTrue(fraudDetectionSystem.getRiskScore() == 0);
    }

    @Test
    public void testCalculateSimilarity() {
    	fraudDetectionBOService.flagTransactionUsername("activity1");
        assertTrue(fraudDetectionBOService.calculateSimilarity("activity1", "asdasdasd") < 1);
    }
    
    @Test
    public void checkPassword() {
    	boolean isCorrect = fraudDetectionBOService.checkPassword("password123");
    	System.out.println(isCorrect);
    	//assertEquals("User [username=johnDoe, name=John Doe, password=password123, transaction_id=1001, type=deposit, amount=250.75, date=12-08-2024 23:22:01, status=completed]".substring(0, 90),user.toString().substring(0, 90));
    }
//    public void checkPasswordSuite() {
//    	User user = fraudDetectionService.checkPassword("johnDoe","password123",true);
//    	System.out.println(user);
//    	assertEquals("User [username=ISoxJQE2Lg==, name=ASoxJWUdJCA=, password=OyQqODI2OSFoeXY=, transaction_id=15607355648, type=LyApJDYwPw==, amount=6.218472233E-315, date=13-08-2024 16:46:30, status=KCo0Oyk8PyA9]".substring(0, 90),user.toString().substring(0, 90));
//    }
}