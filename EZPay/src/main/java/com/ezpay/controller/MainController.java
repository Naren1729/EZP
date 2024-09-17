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
import com.ezpay.exception.InvalidUserException;
import com.ezpay.exception.UserNotFoundException;
import com.ezpay.service.TransactionService;
import com.ezpay.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "https://66df2c1f49107e46015c2321--effervescent-toffee-1278cd.netlify.app",
		"http://localhost:3000" })
public class MainController {

	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;
	private String username;

	// Add a new user
	@PostMapping("/user")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		if (user == null) {
			throw new InvalidUserException("Invalid user data");
		}
		User userAdded = Optional.ofNullable(userService.saveUser(user))
				.orElseThrow(() -> new InvalidUserException("Failed to add user"));
		System.out.println(userAdded);
		return new ResponseEntity<>(userAdded, HttpStatus.CREATED);
	}

	// Get user by ID and decrypt
	@GetMapping("/user/id/{id}")
	public ResponseEntity<User> decryptAndGetUserById(@PathVariable Long id) {
		User user = Optional.ofNullable(userService.findById(id))
				.orElseThrow(() -> new CannotUpdateEncryptionAndDecryptionException("User not found with id: " + id));
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/user/username/{username}")
	public ResponseEntity<User> decryptAndGetUserByname(@PathVariable("username") String username) {
		this.username = username;
		User user = Optional.ofNullable(userService.findByUsername(username))
				.orElseThrow(() -> new EncryptionOrDecryptionException("User not found with username: " + username));
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Get all users
	@GetMapping("/users")
	public ResponseEntity<List<User>> displayUsers() {
		List<User> users = userService.findAll();
		if (users == null || users.isEmpty()) {
			throw new UserNotFoundException("No users found");
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	// Get all transaction details
	@GetMapping("/transactionDetails")
	public ResponseEntity<List<TransactionDetails>> displayTransactionDetails() {
		List<TransactionDetails> transactionDetails = transactionService.findAllTransactions();
		if (transactionDetails == null || transactionDetails.isEmpty()) {
			throw new UserNotFoundException("No transaction Details found");
		}
		return new ResponseEntity<>(transactionDetails, HttpStatus.OK);
	}

	// Get transaction details by ID and decrypt
	@GetMapping("/transactionDetails/{id}")
	public ResponseEntity<TransactionDetails> decryptAndGetTransactionDetailsById(@PathVariable Long id) {
		TransactionDetails decryptedTransactionDetailsStatus = Optional
				.ofNullable(transactionService.getTransactionById(id))
				.orElseThrow(() -> new EncryptionOrDecryptionException("User not found with id: " + id));
		return new ResponseEntity<>(decryptedTransactionDetailsStatus, HttpStatus.OK);
	}

	// Flag a transaction as fraudulent
	@PostMapping("/transaction")
	public String flagTransaction( @RequestBody Transaction transaction) {
		if (transactionService.flagTransaction(transaction))
	    	 return "Transaction Successful";
	     else
	    	 return "Transaction Failed";
	}

	// Get all fraud transaction details
	@GetMapping("/fraudTransactionDetails")
	public ResponseEntity<List<FraudTransactionDetails>> displayFraudTransactionDetails() {
		List<FraudTransactionDetails> fraudTransactionDetails = transactionService.findAllFraudTransactions();
		if (fraudTransactionDetails == null || fraudTransactionDetails.isEmpty()) {
			throw new UserNotFoundException("No transaction Details found");
		}
		return new ResponseEntity<>(fraudTransactionDetails, HttpStatus.OK);
	}

	// Get fraud transaction details by ID and decrypt
	@GetMapping("/fraudTransactionDetails/{id}")
	public ResponseEntity<FraudTransactionDetails> decryptAndGetFraudTransactionDetailsById(@PathVariable Long id) {
		FraudTransactionDetails decryptedFraudTransactionDetailsStatus = Optional
				.ofNullable(transactionService.getFraudTransactionDetailsById(id))
				.orElseThrow(() -> new EncryptionOrDecryptionException("User not found with id: " + id));
		return new ResponseEntity<>(decryptedFraudTransactionDetailsStatus, HttpStatus.OK);
	}

	// Authenticate user
	@PostMapping("/authenticate")
	public String authenticateUser(@RequestBody Map<String, String> request) {
		String username = request.get("username");
		String password = request.get("password");
		if (userService.authenticate(username, password))
			return "Login Successful";
		else
			return "Login Failed";
	}

	// Add multiple users
	@PostMapping("/users")
	public List<User> addUsers(@RequestBody List<User> users) {
		if (users == null || users.isEmpty()) {
			return null;
		}
		List<User> addedUsers = new ArrayList<>();
		for (User user : users) {
			User userAdded = userService.saveUser(user);
			if (userAdded != null) {
				addedUsers.add(userAdded);
			}
		}
		return addedUsers;
	}
	
	@PutMapping("/users/{userId}")
	public ResponseEntity<String> updateUsers(@PathVariable Long userId,@RequestBody User user){
		User userExists = userService.updateUser(userId, user);
		if (userExists == null) {
			throw new EncryptionOrDecryptionException("User not found with id: " + userId);
		}
		return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
		
	}
	
	@PatchMapping("/users/{userId}")
	public ResponseEntity<String> changeUsers(@PathVariable Long userId, @RequestBody Map<String,Object>update){
		User changedUser = userService.changeUser(userId,update);
		if(changedUser==null) {
			throw new EncryptionOrDecryptionException("User not found with id: " + userId);
		}
		return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteUsers(@PathVariable Long userId){
		boolean response = userService.deleteUsers(userId);
		if(!response) {
			throw new EncryptionOrDecryptionException("User not found with id: " + userId);
		}
		return new ResponseEntity<String>("Deleted Successfully",HttpStatus.OK);
	}
}
