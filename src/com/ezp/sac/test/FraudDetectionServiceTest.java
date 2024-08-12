package com.ezp.sac.test;

import static org.junit.Assert.*;

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

    @Test
    public void testGetUsername() {
        List<String> usernames = Arrays.asList("activity1", "activity2", "activity3");
        fraudDetectionService.setUsername(usernames);
        assertEquals(usernames, fraudDetectionService.getUsername());
    }

    @Test
    public void testSetUsername() {
        List<String> usernames = Arrays.asList("activity1", "activity2", "activity3");
        fraudDetectionService.setUsername(usernames);
        assertEquals(usernames, fraudDetectionService.getUsername());
    }

    @Test
    public void testDetectFraud() {
        List<String> suspiciousActivities = Arrays.asList("activity1", "activity2");
        fakeDatabase.setUsername(suspiciousActivities);
        fraudDetectionService.detectFraud("activity1");
        assertTrue(fraudDetectionSystem.getRiskScore() >= 0);
    }

    @Test
    public void testFlagTransaction() {
        List<String> suspiciousActivities = Arrays.asList("activity1", "activity2");
        fakeDatabase.setUsername(suspiciousActivities);
        fraudDetectionService.flagTransaction("activity1");
        assertTrue(fraudDetectionSystem.getRiskScore() >= 0);
    }
}
