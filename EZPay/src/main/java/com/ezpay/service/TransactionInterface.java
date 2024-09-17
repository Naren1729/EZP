/**
 * @author: Naren, Bhavansh
 * @date: 2/09/2024
 * 
 * @description:
 * TransactionInterface defines methods for managing and processing transactions.
 * 
 * This interface outlines operations related to transactions, including fetching transaction details,
 * flagging transactions, and calculating similarities between transaction descriptions.
 */

package com.ezpay.service;

import java.math.BigDecimal;
import java.util.List;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.Transaction;
import com.ezpay.entity.TransactionDetails;

public interface TransactionInterface {

	/**
	 * Retrieves a TransactionDetails object by its ID.
	 * 
	 * @param transactionId The ID of the transaction to retrieve.
	 * @return The TransactionDetails object associated with the specified ID.
	 */
	public TransactionDetails getTransactionById(Long transactionId);

	/**
	 * Retrieves a FraudTransactionDetails object by its ID.
	 * 
	 * @param id The ID of the fraud transaction details to retrieve.
	 * @return The FraudTransactionDetails object associated with the specified ID.
	 */
	public FraudTransactionDetails getFraudTransactionDetailsById(Long id);

	/**
	 * Flags a transaction as fraudulent or suspicious.
	 * 
	 * @param transaction The Transaction object to be flagged.
	 * @return object of TransactionDetails class with status of transaction
	 */
	public boolean flagTransaction(Transaction transaction);

	/**
	 * Retrieves a list of all TransactionDetails objects.
	 * 
	 * @return A list of all TransactionDetails objects.
	 */
	public List<TransactionDetails> findAllTransactions();

	/**
	 * Retrieves a list of all FraudTransactionDetails objects.
	 * 
	 * @return A list of all FraudTransactionDetails objects.
	 */
	public List<FraudTransactionDetails> findAllFraudTransactions();

	/**
	 * Calculates the similarity between two strings.
	 * 
	 * @param str1 The first string.
	 * @param str2 The second string.
	 * @return A BigDecimal value representing the similarity score between the two
	 *         strings.
	 */
	public BigDecimal calculateSimilarity(String str1, String str2);
}
