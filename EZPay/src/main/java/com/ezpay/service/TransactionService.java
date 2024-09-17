/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * */

package com.ezpay.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.Transaction;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;
import com.ezpay.exception.EncryptionOrDecryptionException;
import com.ezpay.exception.UserNotFoundException;
import com.ezpay.repository.FraudRepo;
import com.ezpay.repository.TransactionRepo;
import com.ezpay.repository.UserRepo;

@Service
public class TransactionService implements TransactionInterface {

	@Autowired
	private UserService userService; // Service to handle user-related operations

	@Autowired
	private TransactionRepo transactionRepo; // Repository for transaction details

	@Autowired
	private UserRepo userRepo; // Repository for user details

	@Autowired
	private FraudRepo fraudRepo; // Repository for fraud transaction details

	@Autowired
	private EncryptionBOService encryptionservice; // Service for encrypting data

	@Autowired
	private DecryptionBOService decryptionservice; // Service for decrypting data

	/**
	 * Retrieves a transaction by its ID and decrypts the details.
	 * 
	 * @param Transaction_Id The ID of the transaction.
	 * @return Decrypted transaction details.
	 * @throws UserNotFoundException           if no transaction is found with the
	 *                                         given ID.
	 * @throws EncryptionOrDecryptionException if decryption fails.
	 */
	public TransactionDetails getTransactionById(Long Transaction_Id) {
		// Fetch transaction details by ID
		TransactionDetails transactionDetails = transactionRepo.findById(Transaction_Id)
				.orElseThrow(() -> new UserNotFoundException("Id Invalid"));

		// Decrypt transaction details
		TransactionDetails decryptedTransactionDetails = decryptionservice.decryptTransaction(transactionDetails);
		return decryptedTransactionDetails;
	}

	/**
	 * Retrieves fraud transaction details by its ID and decrypts the details.
	 * 
	 * @param id The ID of the fraud transaction.
	 * @return Decrypted fraud transaction details.
	 * @throws UserNotFoundException           if no fraud transaction is found with
	 *                                         the given ID.
	 * @throws EncryptionOrDecryptionException if decryption fails.
	 */
	public FraudTransactionDetails getFraudTransactionDetailsById(Long id) {
		// Fetch fraud transaction details by ID
		FraudTransactionDetails fraudTransactionDetails = fraudRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Id Invalid"));

		// Decrypt fraud transaction details
		FraudTransactionDetails decryptedFraudTransactionDetails = decryptionservice
				.decryptFraud(fraudTransactionDetails);
		return decryptedFraudTransactionDetails;
	}
	
	
	public List<TransactionDetails> getTransactionsForUserWithinSameDay(Long usernId, Long destinationUserId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(LocalTime.MAX);
        return transactionRepo.findByUsernIdAndDestinationUserIdAndTransactionTimeBetween(usernId, destinationUserId, startOfDay, endOfDay);
    }
	

	public BigDecimal detectFraud(TransactionDetails transactionDetails) {
	    
	    Long userid = transactionDetails.getUsernId();
	    Long destinationId = transactionDetails.getDestinationUserId();
	    LocalDateTime date = transactionDetails.getTransactionTime();
	    
	    List<TransactionDetails> transactionList = getTransactionsForUserWithinSameDay(userid, destinationId, date);
	    
	    BigDecimal riskscore = BigDecimal.ZERO;

	    LocalTime transactionTime = transactionDetails.getTransactionTime().toLocalTime();
	    LocalTime startOddHours = LocalTime.of(0, 0);
	    LocalTime endOddHours = LocalTime.of(5, 0);

	    if (transactionTime.isAfter(startOddHours) && transactionTime.isBefore(endOddHours)) {
	        if(transactionList.size() == 0) {
	            riskscore = riskscore.add(BigDecimal.ZERO);
	        } else if(transactionList.size() == 1) {
	        	System.out.println("hI");
	            riskscore = riskscore.add(new BigDecimal("50"));
	        } else if(transactionList.size() == 2) {
	        	System.out.println("hI");
	            riskscore = riskscore.add(new BigDecimal("100"));
	        }
	    } else {
	    	System.out.println("nOT IN ODD HOURS");
	        if(transactionList.size() == 0) {
	            riskscore = riskscore.add(BigDecimal.ZERO);
	        } else if(transactionList.size() == 2) {
	        	System.out.println("If transaction is done 2");
	            riskscore = riskscore.add(new BigDecimal("25"));
	        } else if(transactionList.size() == 3) {
	        	System.out.println("If transaction is done 3");
	            riskscore = riskscore.add(new BigDecimal("50"));
	        } else if(transactionList.size() == 4) {
	        	System.out.println("If transaction is done 4");
	            
	            riskscore = riskscore.add(new BigDecimal("75"));
	        } else if(transactionList.size() == 5) {
	        	System.out.println("If transaction is done 5");
	            
	            riskscore = riskscore.add(new BigDecimal("100"));
	        }
	    }
	    if(transactionDetails.getAmount().compareTo(new BigDecimal("50000")) > 0) {
	    	System.out.println("If amount greater than 50000");
	        riskscore = riskscore.add(new BigDecimal("25"));
	    }
	    User user = userService.findById(userid);
	    User destinationUser = userService.findById(destinationId);
	    
	    if(user.getIsBlockeListed()) {
	        return new BigDecimal("100");
	        
	    } else if(destinationUser.getIsBlockeListed()) {
	        riskscore = riskscore.add(new BigDecimal("50"));
	    }
	    
	    // Check if total transaction amount per user per day exceeds 1 lakh
	    BigDecimal totalAmountForDay = getTotalTransactionAmountForUserWithinSameDay(userid,destinationId ,date);
	    if(totalAmountForDay.add(transactionDetails.getAmount()).compareTo(new BigDecimal("200000")) > 0) {
	    	System.out.println("If amount greater than 200000");
	        riskscore = riskscore.add(new BigDecimal("75"));
	    }
	    
	    return riskscore;
	}

	private BigDecimal getTotalTransactionAmountForUserWithinSameDay(Long userId,Long destinationId ,LocalDateTime date) {
	    // Fetch all transactions for the user within the same day
	    List<TransactionDetails> transactions = getTransactionsForUserWithinSameDay(userId, destinationId, date);
	    System.out.println(transactions.size());
	    // Calculate the total amount
	    BigDecimal totalAmount = BigDecimal.ZERO;
	    for (TransactionDetails transaction : transactions) {
	        totalAmount = totalAmount.add(decryptionservice.decryptBigDecimal(transaction.getAmount()));
	        System.out.println(totalAmount);
	    }
	    return totalAmount;
	}
	
	

	/**
	 * Flags a transaction based on various conditions and updates the transaction
	 * status.
	 * 
	 * @param transaction The transaction to be flagged.
	 * @return true if the transaction is successful, false otherwise.
	 */
	public boolean flagTransaction(Transaction transaction) {
		BigDecimal amount = transaction.getAmount();
		Long userId = transaction.getUserID();
		Long destinationUserId = transaction.getDestinationUserID();
		String transactionType = transaction.getType();
		String status = "";
		if(userId == destinationUserId) {
			return false;
		}
		boolean pass = false;
		boolean blackList = false;
		BigDecimal riskScore = BigDecimal.ZERO;
		// Create a new TransactionDetails object
		TransactionDetails transactionDetails1 = new TransactionDetails();
		transactionDetails1.setAmount(amount);
		transactionDetails1.setDestinationUserId(destinationUserId);
		transactionDetails1.setTransactionType(transactionType);
		transactionDetails1.setUsernId(userId);
		transactionDetails1.setTransactionTime(LocalDateTime.now());

		// Retrieve user and destination user details
		User user = userService.findById(userId);
		User destinationUser = userService.findById(destinationUserId);

		// Check if users are blacklisted
		boolean isBlackListeduser = user.getIsBlockeListed();
		boolean isBlackListedDestinationUser = destinationUser.getIsBlockeListed();

		blackList = !isBlackListeduser && !isBlackListedDestinationUser;

		// Check transaction password validity
		String transactionPassword = transaction.getTransactionPassword();
		String userTransactionPassword = user.getTransactionPassword();
		pass = transactionPassword.equals(userTransactionPassword);

		// Get current balance of the user
		BigDecimal userCurrentBalance = user.getCurrentBalance();

		// Check various conditions for the transaction
		if (amount.compareTo(userCurrentBalance) > 0 || amount.compareTo(BigDecimal.ZERO) < 0
				|| amount.compareTo(new BigDecimal("100000")) >= 0 || !pass
				|| userCurrentBalance.compareTo(BigDecimal.ZERO) < 0) {
			status = "failed";
			transactionDetails1.setTransactionStatus(status);
			TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
			TransactionDetails failedTransactionAdded = transactionRepo.save(encryptedTransaction);
//			
//			FraudTransactionDetails fraudTransactionDetails = new FraudTransactionDetails();
//			fraudTransactionDetails.setRiskScore(new BigDecimal("100"));
//			fraudTransactionDetails.setTransaction(transactionDetails1);
//			FraudTransactionDetails encryptedFraudTransaction = encryptionservice
//					.encryptFraudTransaction(fraudTransactionDetails);
//			fraudRepo.save(encryptedFraudTransaction);

			//User encryptUser = encryptionservice.encryptUser(user);
			//User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
			userRepo.save(user);
			userRepo.save(destinationUser);

			return false;
		} else {
			
			status = "Success";
			transactionDetails1.setTransactionStatus(status);
			
			riskScore = detectFraud(transactionDetails1);
			// Update user balances
			if(riskScore.compareTo(new BigDecimal(100)) >= 0) {
				status ="Failed";
				transactionDetails1.setTransactionStatus(status);
				TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
				TransactionDetails failedTransactionAdded = transactionRepo.save(encryptedTransaction);
				user.setBlockeListed(true);
				// Encrypt and save updated user details
				//User encryptUser = encryptionservice.encryptUser(user);
				//User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
				
				FraudTransactionDetails fraudTransactionDetails = new FraudTransactionDetails();
				fraudTransactionDetails.setRiskScore(riskScore);
				fraudTransactionDetails.setTransaction(transactionDetails1);
				FraudTransactionDetails encryptedFraudTransaction = encryptionservice
						.encryptFraudTransaction(fraudTransactionDetails);
				fraudRepo.save(encryptedFraudTransaction);
				
				userRepo.save(user);
				userRepo.save(destinationUser);
				return false;
				
			}else if(riskScore.compareTo(new BigDecimal(25)) >= 0) {
				TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
				TransactionDetails failedTransactionAdded = transactionRepo.save(encryptedTransaction);
				user.setCurrentBalance(user.getCurrentBalance().subtract(amount));
				destinationUser.setCurrentBalance(destinationUser.getCurrentBalance().add(amount));

				// Encrypt and save updated user details
				FraudTransactionDetails fraudTransactionDetails = new FraudTransactionDetails();
				fraudTransactionDetails.setRiskScore(riskScore);
				fraudTransactionDetails.setTransaction(transactionDetails1);
				FraudTransactionDetails encryptedFraudTransaction = encryptionservice
						.encryptFraudTransaction(fraudTransactionDetails);
				fraudRepo.save(encryptedFraudTransaction);

				// Encrypt and save user details
				//User encryptUser = encryptionservice.encryptUser(user);
				//User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
				userRepo.save(user);
				userRepo.save(destinationUser);

				return true;
			}else {
				TransactionDetails encryptedTransaction = encryptionservice.encryptTransaction(transactionDetails1);
				TransactionDetails successTransactionAdded = transactionRepo.save(encryptedTransaction);
				user.setCurrentBalance(user.getCurrentBalance().subtract(amount));
				destinationUser.setCurrentBalance(destinationUser.getCurrentBalance().add(amount));
				
				//User encryptUser = encryptionservice.encryptUser(user);
				//User encryptDestinationUser = encryptionservice.encryptUser(destinationUser);
				userRepo.save(user);
				userRepo.save(destinationUser);
				return true;
			}

		}

	}

	/**
	 * Retrieves and decrypts all transaction details from the repository.
	 * 
	 * @return List of decrypted transaction details.
	 */
	public List<TransactionDetails> findAllTransactions() {
		List<TransactionDetails> encryptedTransactions = transactionRepo.findAll();
		List<TransactionDetails> decryptedTransactions = new ArrayList<>();
		for (TransactionDetails transaction : encryptedTransactions) {
			decryptedTransactions.add(decryptionservice.decryptTransaction(transaction));
		}
		return decryptedTransactions;
	}

	/**
	 * Retrieves and decrypts all fraud transaction details from the repository.
	 * 
	 * @return List of decrypted fraud transaction details.
	 */
	public List<FraudTransactionDetails> findAllFraudTransactions() {
		List<FraudTransactionDetails> encryptedFraudTransactions = fraudRepo.findAll();
		List<FraudTransactionDetails> decryptedFraudTransactions = new ArrayList<>();
		for (FraudTransactionDetails fraudTransaction : encryptedFraudTransactions) {
			decryptedFraudTransactions.add(decryptionservice.decryptFraud(fraudTransaction));
		}
		return decryptedFraudTransactions;
	}

	/**
	 * Calculates the similarity between two strings.
	 * 
	 * @param str1 First string.
	 * @param str2 Second string.
	 * @return Similarity score as a BigDecimal.
	 */
	public BigDecimal calculateSimilarity(String str1, String str2) {
		int intersection = 0;
		int union = str1.length() + str2.length();

		for (char c : str1.toCharArray()) {
			if (str2.indexOf(c) != -1) {
				intersection++;
			}
		}

		return new BigDecimal(intersection).divide(new BigDecimal(union - intersection), 2, RoundingMode.HALF_UP);
	}
}