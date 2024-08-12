//Author: Keerthana
package com.ezp.sac.test;

import com.ezp.sac.service.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EncryptionBOServiceTest {
	static private EncryptionBOService encryptionServiceTestObject;
	String encryptionAlgorithm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		encryptionServiceTestObject = new EncryptionBOService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
		assertNotNull("The service package class object is not null", encryptionServiceTestObject);
	}

	@Test
	public void testEncryptUserData() {
		// Test to check if correct input returns a non-null value
		assertNotNull(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "johnDoe"));
		assertNotNull(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "janeSmith"));

		// Test to check if incorrect input returns a null value
		assertNull(encryptionServiceTestObject.encryptUserData("Ceaser Cipher", "bobBrown")); // Incorrect encryption algorithm
		assertNull(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "Keerthana")); // No such username found in the database
		assertNull(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "johnDOE")); // Incorrect case for username

		// Test to verify that the encryption produces the expected output
		// Correct input -> correct output
		assertEquals(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "aliceJones").toString().substring(0, 140),
				"User [username=KikwKCATJCs8OA==, name=CikwKCB5ASo3LjY=, password=OyQqODI2OSFuc3w=, transaction_id=15573801216, type=LyApJDYwPw==, amount=6.218451877E-315, date=10-08-2024 17:19:40, status=KCo0Oyk8PyA9]"
						.substring(0, 140));

		// Incorrect input -> correct output
		assertNotEquals(encryptionServiceTestObject.encryptUserData(encryptionAlgorithm, "carolWhite").toString().substring(0, 140),
				"User [username=XYZ7CTc2PCs=, name=CSo7awcrJDI3, password=OyQqODI2OSFpenc=, transaction_id=15691241728, type=Pzc4JTY/Ljc=, amount=6.21847911E-315, date=10-08-2024 17:08:14, status=LSQwJyA9]"
						.substring(0, 140));

	}

	@Test
	public void testGetAllUsers() {
		// Test to check if the user list is non-empty
		assertTrue(encryptionServiceTestObject.getAllUsers().size() > 0);
		// Test to verify that the total number of users is five
		assertEquals(8, encryptionServiceTestObject.getAllUsers().size());
	}

}