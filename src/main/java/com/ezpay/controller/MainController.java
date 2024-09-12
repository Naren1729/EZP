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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.Transaction;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;
import com.ezpay.exception.EncryptionOrDecryptionException;
import com.ezpay.exception.InvalidUserException;
import com.ezpay.exception.UserNotFoundException;
import com.ezpay.service.TransactionService;
import com.ezpay.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class MainController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;
	
	// Add a new user
	@PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user == null ) {
            throw new InvalidUserException("Invalid user data");
        }
        User userAdded = Optional.ofNullable(userService.saveUser(user)).orElseThrow(()->new InvalidUserException("Failed to add user"));
        return new ResponseEntity<>(userAdded, HttpStatus.CREATED);
    }
	
	// Get user by ID and decrypt
	@GetMapping("/user/{id}")
	public ResponseEntity<User> decryptAndGetUserById(@PathVariable Long id) {
		User user = Optional.ofNullable(userService.findById(id)).orElseThrow(()->new EncryptionOrDecryptionException("User not found with id: " + id));
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
		TransactionDetails decryptedTransactionDetailsStatus = Optional.ofNullable(transactionService.getTransactionById(id)).orElseThrow(()->new EncryptionOrDecryptionException("User not found with id: " + id));
		return new ResponseEntity<>(decryptedTransactionDetailsStatus, HttpStatus.OK);
	}

	// Flag a transaction as fraudulent
	@PostMapping("/transaction")
	public ResponseEntity<TransactionDetails> flagTransaction( @RequestBody Transaction transaction) {
		TransactionDetails addedTransaction = transactionService.flagTransaction(transaction);
        return new ResponseEntity<>(addedTransaction, HttpStatus.CREATED);
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
		FraudTransactionDetails decryptedFraudTransactionDetailsStatus = Optional.ofNullable(transactionService.getFraudTransactionDetailsById(id)).orElseThrow(()->new EncryptionOrDecryptionException("User not found with id: " + id));
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
}
