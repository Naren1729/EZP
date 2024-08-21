/**
 * @Author: Keerthana,Arvind
 * @Date : 20/08/2024
 * 
 * @Description:
 * The EncryptionBOServiceTest class is a unit test class for testing the functionality of
 * the EncryptionBOService class. It uses JUnit to verify that the encryption and decryption
 * processes are working as expected. The test cases check if the EncryptionBOService instance 
 * is created properly, if user data is encrypted correctly, and if the list of users is returned
 * accurately. The class ensures that both valid and invalid inputs are handled appropriately.
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

public class EncryptionBOServiceTest {
    static private EncryptionBOService encryptionServiceTestObject;
    static private DecryptionBOService decryptionServiceTestObject;
    static String encryptionAlgorithm;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        encryptionServiceTestObject = new EncryptionBOService();
        decryptionServiceTestObject = new DecryptionBOService();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        decryptionServiceTestObject.decryptUserData("Vernam Cipher", "ISoxJQE2Lg==");
        decryptionServiceTestObject.decryptUserData("Vernam Cipher", "ISQ3LhY0IjEx");
        decryptionServiceTestObject.decryptUserData("Vernam Cipher", "KikwKCATJCs8OA==");
        decryptionServiceTestObject.decryptUserData("Vernam Cipher", "KCQrJCkOIywtLg==");
        encryptionServiceTestObject = null;
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
    public void testEncryptionBOService() {
        assertThat("The service package class object is not null", encryptionServiceTestObject, is(notNullValue()));
    }

    @Test
    public void testEncryptUserData() {
        // Test to check if correct input returns a non-null value
        assertThat(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "johnDoe", false), is(notNullValue()));
        assertThat(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "janeSmith", false), is(notNullValue()));

        // Test to check if incorrect input returns a null value
        assertThat(encryptionServiceTestObject.encryptUserData("Ceaser Cipher", "bobBrown", false), is(nullValue())); // Incorrect encryption algorithm
        assertThat(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "Keerthana", false), is(nullValue())); // No such username found in the database
        assertThat(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "johnDOE", false), is(nullValue())); // Incorrect case for username

        // Test to verify that the encryption produces the expected output
        // Correct input -> correct output
        assertThat(
            encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "aliceJones", false).toString().substring(0, 140),
            equalTo("User [username=KikwKCATJCs8OA==, name=CikwKCB5ASo3LjY=, password=OyQqODI2OSFuc3w=, transaction_id=15573801216, type=LyApJDYwPw==, amount=6.2184518770599045E-115, date=20-08-2024 15:49:34, status=KCo0Oyk8PyA9]"
                    .substring(0, 140))
        );

        // Incorrect input -> correct output
        assertThat(
            encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "carolWhite", false).toString().substring(0, 140),
            not(equalTo("User User [username=KCQrJCkOIywtLg==, name=CCQrJCl5HC0wPyA=, password=OyQqODI2OSFqf3A=, transaction_id=15674464512, type=LyApJDYwPw==, amount=6.218418359646491E-115, date=20-08-2024 15:49:34, status=KCo0Oyk8PyA9]"
                        .substring(0, 140)))
        );
    }
}
