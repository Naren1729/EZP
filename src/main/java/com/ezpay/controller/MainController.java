/**
 * @author: Naren
 * 
 * @description:
 * 
 * MainController handles user and transaction-related requests.
 * 
 * Endpoints provided:
 * - Add, retrieve, and decrypt user details.
 * - Display all users and transaction details.
 * - Flag transactions as fraudulent.
 * - Authenticate users.
 * 
 * Exceptions handled include EncryptionOrDecryptionException, InvalidUserException, and UserNotFoundException.
 * All endpoints are prefixed with "/main" and allow cross-origin requests from "http://localhost:3000".
 */

package com.ezpay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.Transaction;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;
import com.ezpay.exception.CannotUpdateEncryptionAndDecryptionException;
import com.ezpay.exception.EncryptionOrDecryptionException;
import com.ezpay.exception.UserNotFoundException;
import com.ezpay.service.TransactionService;
import com.ezpay.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "https://66df2c1f49107e46015c2321--effervescent-toffee-1278cd.netlify.app",
		"http://localhost:3000" })
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;
	
	private static final String CONSTUSERNOTFOUND= "User not found with id: ";


	// Add a new user
	@PostMapping("/user")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		if (user == null) {
			logger.error("Invalid user data");
			throw new EncryptionOrDecryptionException("Invalid user data");
		}
		User userAdded = Optional.ofNullable(userService.saveUser(user))
				.orElseThrow(() -> new CannotUpdateEncryptionAndDecryptionException("Failed to add user"));
		logger.info("User added: {}", userAdded);
		return new ResponseEntity<>(userAdded, HttpStatus.CREATED);
	}

	// Get user by ID and decrypt
	@GetMapping("/user/id/{id}")
	public ResponseEntity<User> decryptAndGetUserById(@PathVariable Long id) {
		User user = Optional.ofNullable(userService.findById(id))
				.orElseThrow(() -> new EncryptionOrDecryptionException(CONSTUSERNOTFOUND + id));
		logger.info("User found and decrypted with id: {}", id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/user/username/{username}")
	public ResponseEntity<User> decryptAndGetUserByUsername(@PathVariable("username") String username) {
		User user = Optional.ofNullable(userService.findByUsername(username))
				.orElseThrow(() -> new EncryptionOrDecryptionException("User not found with username: " + username));
		logger.info("User found and decrypted with username: {}", username);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Get all users
	@GetMapping("/users")
	public ResponseEntity<List<User>> displayUsers() {
		List<User> users = userService.findAll();
		if (users == null || users.isEmpty()) {
			logger.error("No users found");
			throw new UserNotFoundException("No users found");
		}
		logger.info("Displaying all users");
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	// Get all transaction details
	@GetMapping("/transactionDetails")
	public ResponseEntity<List<TransactionDetails>> displayTransactionDetails() {
		List<TransactionDetails> transactionDetails = transactionService.findAllTransactions();
		if (transactionDetails == null || transactionDetails.isEmpty()) {
			logger.error("No transaction details found");
			throw new UserNotFoundException("No transaction details found");
		}
		logger.info("Displaying all transaction details");
		return new ResponseEntity<>(transactionDetails, HttpStatus.OK);
	}

	// Get transaction details by ID and decrypt
	@GetMapping("/transactionDetails/{id}")
	public ResponseEntity<TransactionDetails> decryptAndGetTransactionDetailsById(@PathVariable Long id) {
		TransactionDetails decryptedTransactionDetails = Optional
				.ofNullable(transactionService.getTransactionById(id))
				.orElseThrow(() -> new EncryptionOrDecryptionException("Transaction not found with id: " + id));
		logger.info("Transaction details decrypted with id: {}", id);
		return new ResponseEntity<>(decryptedTransactionDetails, HttpStatus.OK);
	}

	// Flag a transaction as fraudulent
	@PostMapping("/transaction")
	public String flagTransaction(@RequestBody Transaction transaction) {
		if (transactionService.flagTransaction(transaction)) {
			logger.info("Transaction : {}", transaction);
			return "Transaction Successful";
		} else {
			logger.error("Failed to flag transaction: {}", transaction);
			return "Transaction Failed";
		}
	}

	// Get all fraud transaction details
	@GetMapping("/fraudTransactionDetails")
	public ResponseEntity<List<FraudTransactionDetails>> displayFraudTransactionDetails() {
		List<FraudTransactionDetails> fraudTransactionDetails = transactionService.findAllFraudTransactions();
		if (fraudTransactionDetails == null || fraudTransactionDetails.isEmpty()) {
			logger.error("No fraud transaction details found");
			throw new UserNotFoundException("No fraud transaction details found");
		}
		logger.info("Displaying all fraud transaction details");
		return new ResponseEntity<>(fraudTransactionDetails, HttpStatus.OK);
	}

	// Get fraud transaction details by ID and decrypt
	@GetMapping("/fraudTransactionDetails/{id}")
	public ResponseEntity<FraudTransactionDetails> decryptAndGetFraudTransactionDetailsById(@PathVariable Long id) {
		FraudTransactionDetails decryptedFraudTransactionDetails = Optional
				.ofNullable(transactionService.getFraudTransactionDetailsById(id))
				.orElseThrow(() -> new EncryptionOrDecryptionException("Fraud transaction not found with id: " + id));
		logger.info("Fraud transaction details decrypted with id: {}", id);
		return new ResponseEntity<>(decryptedFraudTransactionDetails, HttpStatus.OK);
	}

	// Authenticate user
	@PostMapping("/authenticate")
	public String authenticateUser(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		String password = request.get("password");
		if (userService.authenticate(username, password)) {
			logger.info("User authenticated successfully: {}", username);
			return "Login Successful";
		} else {
			logger.error("Authentication failed for user: {}", username);
			return "Login Failed";
		}
	}

	// Add multiple users
	@PostMapping("/users")
	public List<User> addUsers(@RequestBody List<User> users) {
		if (users == null || users.isEmpty()) {
			logger.error("Invalid user list");
			return new ArrayList<>();
		}
		List<User> addedUsers = new ArrayList<>();
		for (User user : users) {
			User userAdded = userService.saveUser(user);
			if (userAdded != null) {
				addedUsers.add(userAdded);
			}
		}
		logger.info("Added multiple users");
		return addedUsers;
	}

	// Update a user
	@PutMapping("/users/{userId}")
	public ResponseEntity<String> updateUsers(@PathVariable Long userId, @RequestBody User user) {
		User userExists = userService.updateUser(userId, user);
		if (userExists == null) {
			logger.error(CONSTUSERNOTFOUND, userId);
			throw new EncryptionOrDecryptionException(CONSTUSERNOTFOUND + userId);
		}
		logger.info("Updated user with id: {}", userId);
		return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
	}

	// Partially update a user
	@PatchMapping("/users/{userId}")
	public ResponseEntity<String> changeUsers(@PathVariable Long userId, @RequestBody Map<String, Object> update) {
		User changedUser = userService.changeUser(userId, update);
		if (changedUser == null) {
			logger.error(CONSTUSERNOTFOUND, userId);
			throw new EncryptionOrDecryptionException(CONSTUSERNOTFOUND + userId);
		}
		logger.info("Partially updated user with id: {}", userId);
		return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
	}

	// Delete a user
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteUsers(@PathVariable Long userId) {
		boolean response = userService.deleteUsers(userId);
		if (!response) {
			logger.error(CONSTUSERNOTFOUND, userId);
			throw new EncryptionOrDecryptionException(CONSTUSERNOTFOUND + userId);
		}
		logger.info("Deleted user with id: {}", userId);
		return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
	}

}
