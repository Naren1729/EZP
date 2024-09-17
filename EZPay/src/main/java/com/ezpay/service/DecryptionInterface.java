/**
 * @author: Keerthana
 * @date: 3/09/2024
 * 
 * @description:
 * DecryptionInterface defines the contract for decryption services.
 * 
 * This interface specifies methods for decrypting various types of sensitive data,
 * including user information, transaction details, and fraud transaction details.
 * Implementing classes must provide concrete implementations for these methods,
 * which handle decryption of data encrypted with a specific algorithm or key.
 */

package com.ezpay.service;

import java.math.BigDecimal;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;


public interface DecryptionInterface {

    /**
     * Decrypts a User object, including fields such as username, password, email,
     * current balance, and transaction password.
     */
//    public User decryptUser(User user);

    /**
     * Decrypts a TransactionDetails object, including fields such as amount,
     * transaction type, and transaction status.
     */
    public TransactionDetails decryptTransaction(TransactionDetails transactionDetails);

    /**
     * Decrypts a FraudTransactionDetails object, which includes the associated
     * TransactionDetails and risk score.
     */
    public FraudTransactionDetails decryptFraud(FraudTransactionDetails fraudTransaction);

    /**
     * Decrypts a Long value using a specific algorithm or key.
     */
    public Long decryptLong(Long val);

    /**
     * Decrypts a BigDecimal value by processing its unscaled value with decryption operations.
     */
    public BigDecimal decryptBigDecimal(BigDecimal val);

    /**
     * Decrypts a String value using Base64 decoding and XOR with a specific key.
     */
    public String decrypt(String val);

}
