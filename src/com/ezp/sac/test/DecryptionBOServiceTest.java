/**
 * @Author: Keerthana,Arvind
 * @Date : 20/08/2024
 * 
 * @Description:
 * The DecryptionBOServiceTest class is a JUnit test class designed to validate the functionality
 * of the DecryptionBOService class. It includes tests to verify that user data is correctly decrypted
 * using the specified encryption algorithm. The tests also check the handling of incorrect inputs and
 * ensure that decryption operations produce the expected results.
 */
package com.ezp.sac.test;

import com.ezp.sac.service.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DecryptionBOServiceTest {
    static private DecryptionBOService decryptionServiceTestObject;
    static private EncryptionBOService encryptionServiceTestObject1;
    String encryptionAlgorithm;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        decryptionServiceTestObject = new DecryptionBOService();
        encryptionServiceTestObject1 = new EncryptionBOService();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    	decryptionServiceTestObject.decryptUserData("Vernam Cipher", "KSo7CTc2PCs=");
        decryptionServiceTestObject = null;
    }

    @Before
    public void setUp() throws Exception {
        encryptionAlgorithm = "Vernam Cipher";
    }

    @After
    public void tearDown() throws Exception {
        encryptionAlgorithm = "";
    }

    @Test
    public void testDecryptionBOService() {
        assertThat("The service package class object is not null", decryptionServiceTestObject, is(notNullValue()));
    }

    @Test
    public void testDecryptUserData() {
        // Test to check if correct input returns a non-null value
        assertThat(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
                encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "keerthanaB").getUsername()), is(notNullValue()));
        assertThat(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
                encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "carolWhite").getUsername()), is(notNullValue()));

        // Test to check if incorrect input returns a null value
        assertThat(decryptionServiceTestObject.decryptUserData("Ceaser Cipher",
                encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "bobBrown").getUsername()), is(nullValue())); // Incorrect encryption algorithm
        assertThat(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm, "AHD08ffGR"), is(nullValue())); // No such username found in the database
        assertThat(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm, "KS04PSQ3OC0E"), is(nullValue())); // Incorrect case for username

        // Test to verify that the decryption produces the expected output
        // Correct input -> correct output
        assertThat(
            decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
                    encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "janeSmith").getUsername()).toString().substring(0, 100),
            equalTo("User [username=janeSmith, name=Jane Smith, password=password456, transaction_id=1002, type=withdrawal, amount=100.5, date=11-08-2024 18:45:48, status=pending]"
                    .substring(0, 100))
        );

        // Incorrect input -> correct output
        assertThat(
            decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
                    encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "alisonRose").getUsername()).toString().substring(0, 100),
            not(equalTo("User [username=janeSmith, name=Jane Smith, password=password456, transaction_id=1002, type=withdrawal, amount=100.5, date=11-08-2024 18:45:48, status=pending]"
                    .substring(0, 100)))
        );
    }
}
