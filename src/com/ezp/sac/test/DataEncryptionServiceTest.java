package com.ezp.sac.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ezp.sac.service.*;

class DataEncryptionServiceTest {

	static private DataEncryptionService de;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Object of Service class;
		de = new DataEncryptionService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		de = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDataEncryptionService() {
		assertNotNull("The service package class object is not null", de);

	}

	@Test
    public void testEncryptUserData() {
        // Test to check if correct input returns a non-null encrypted user
        assertNotNull(de.encryptUserData("Vernam Cipher", "johnDoe"));
        assertNotNull(de.encryptUserData("Vernam Cipher", "janeSmith"));

        // Test to check if incorrect input returns a null value
        assertNull(de.encryptUserData("Ceaser Cipher", "bobBrown")); // Incorrect encryption algorithm
        assertNull(de.encryptUserData("Vernam Cipher", "Keerthana")); // No such username found in the database
        assertNull(de.encryptUserData("Vernam Cipher", "johnDOE")); // Incorrect case for username

        // Test to verify that the encryption produces the expected output
        assertEquals(de.encryptUserData("Vernam Cipher", "aliceJones").toString().substring(0, 140), 
            "User [username=KikwKCATJCs8OA==, name=CikwKCB5ASo3LjY=, password=OyQqODI2OSFuc3w=, transaction_id=15573801216, type=LyApJDYwPw==, amount=6.218451877E-315, date=10-08-2024 17:19:40, status=KCo0Oyk8PyA9]".substring(0, 140));

        // Test to ensure incorrect input does not match expected output
        assertNotEquals(de.encryptUserData("Vernam Cipher", "carolWhite").toString().substring(0, 140), 
            "User [username=XYZ7CTc2PCs=, name=CSo7awcrJDI3, password=OyQqODI2OSFpenc=, transaction_id=15691241728, type=Pzc4JTY/Ljc=, amount=6.21847911E-315, date=10-08-2024 17:08:14, status=LSQwJyA9]".substring(0, 140));
    }

    @Test
    public void testDecryptUserData() {
        // Test to check if correct input returns a non-null decrypted user
        assertNotNull(de.decryptUserData("Vernam Cipher", "ISoxJQE2Lg==")); // "johnDoe"
        assertNotNull(de.decryptUserData("Vernam Cipher", "ISQ3LhY0IjEx")); // "janeSmith"

        // Test to check if incorrect input returns a null value
        assertNull(de.decryptUserData("Ceaser Cipher", "ISoxJQE2Lg==")); // Incorrect encryption algorithm
        assertNull(de.decryptUserData("Vernam Cipher", "Bhavansh")); // No such username found in the database
        assertNull(de.decryptUserData("Vernam Cipher", "ISOXJQE2LG==")); // Incorrect case for username

        // Test to verify that the decryption produces the expected output
        assertEquals(
            de.decryptUserData("Vernam Cipher", de.encryptUserData("Vernam Cipher", "bobBrown").getUsername())
            .toString().substring(0, 100),
            "User [username=bobBrown, name=Bob Brown, password=password012, transaction_id=1004, type=transfer, amount=150.25, date=10-08-2024 17:29:39, status=failed]".substring(0, 100));

        // Test to verify that decrypting a specific encrypted username produces the correct output
        assertEquals(de.decryptUserData("Vernam Cipher", "KCQrJCkOIywtLg==").toString().substring(0, 100), 
            "User [username=carolWhite, name=Carol White, password=password345, transaction_id=1005, type=deposit, amount=75.0, date=10-08-2024 17:28:14, status=completed]".substring(0, 100));

        // Test to ensure incorrect input does not match expected output
        assertNotEquals(de.decryptUserData("Vernam Cipher", "KikwKCATJCs8OA==").toString().substring(0, 100), 
            "User [username=carolWhite, name=Carol White, password=password345, transaction_id=1005, type=deposit, amount=75.0, date=10-08-2024 17:28:14, status=completed]".substring(0, 100));
    }

    @Test
    public void testGetAllUsers() {
        // Test to check if the user list is non-empty
        assertTrue(de.getAllUsers().size() > 0);
        // Test to verify that the total number of users is five
        assertEquals(5, de.getAllUsers().size());
    }


}
