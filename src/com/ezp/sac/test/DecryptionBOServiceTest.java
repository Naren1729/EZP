//Author: Keerthana
package com.ezp.sac.test;

import com.ezp.sac.service.*;
import static org.junit.Assert.*;

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
		assertNotNull("The service package class object is not null", decryptionServiceTestObject);
	}

	@Test
	public void testDecryptUserData() {
		// Test to check if correct input returns a non-null value
		assertNotNull(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
				encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "keerthanaB").getUsername())); // "johnDoe"
		assertNotNull(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
				encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "carolWhite").getUsername())); // "janeSmith"

		// Test to check if incorrect input returns a null value
		assertNull(decryptionServiceTestObject.decryptUserData("Ceaser Cipher",
				encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "bobBrown").getUsername())); // Incorrect encryption algorithm
		assertNull(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm, "AHD08ffGR")); // No such username found in the database
		assertNull(decryptionServiceTestObject.decryptUserData(encryptionAlgorithm, "KS04PSQ3OC0E")); // Incorrect case for username

		// Test to verify that the decryption produces the expected output
		// Correct input -> correct output
		assertEquals(
				decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
						encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "janeSmith").getUsername()).toString()
						.substring(0, 100),
				"User [username=janeSmith, name=Jane Smith, password=password456, transaction_id=1002, type=withdrawal, amount=100.5, date=11-08-2024 18:45:48, status=pending]"
						.substring(0, 100));
		// Incorrect input -> correct output
		assertNotEquals(
				decryptionServiceTestObject.decryptUserData(encryptionAlgorithm,
						encryptionServiceTestObject1.encryptUserData(encryptionAlgorithm, "alisonRose").getUsername()).toString()
						.substring(0, 100),
				"User [username=janeSmith, name=Jane Smith, password=password456, transaction_id=1002, type=withdrawal, amount=100.5, date=11-08-2024 18:45:48, status=pending]"
						.substring(0, 100));

	}

}