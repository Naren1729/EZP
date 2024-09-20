/**
 * @author: Keerthana
 * @date: 2/09/2024
 * 
 * @description:
 * EncryptionBOService provides methods for encrypting various types of sensitive data.
 * 
 * This service handles encryption for user information, transaction details, and fraud transaction details
 * using a specified encryption key. It implements the EncryptionInterface and applies encryption techniques
 * to various data types.
 */

package com.ezpay.service;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezpay.entity.FraudTransactionDetails;
import com.ezpay.entity.TransactionDetails;
import com.ezpay.repository.UserRepo;


@Service
public class EncryptionBOService implements EncryptionInterface {

    private final String encryptionKey = "KEY";

    @Autowired
    public UserRepo userRepo;


    /**
     * Encrypts a TransactionDetails object, including fields such as amount,
     * transaction type, and transaction status.
     * 
     * @param transactionDetails The TransactionDetails object containing plaintext data.
     * @return The TransactionDetails object with encrypted data.
     */
    public TransactionDetails encryptTransaction(TransactionDetails transactionDetails) {
        transactionDetails.setAmount(encryptBigDecimal(transactionDetails.getAmount()));
        transactionDetails.setDestinationUserId(transactionDetails.getDestinationUserId());
        transactionDetails.setUsernId(transactionDetails.getUsernId());
        transactionDetails.setTransactionType(encrypt(transactionDetails.getTransactionType()));
        transactionDetails.setTransactionStatus(encrypt(transactionDetails.getTransactionStatus()));
        return transactionDetails;
    }
    
    public FraudTransactionDetails encryptFraudTransaction(FraudTransactionDetails fraudTransactionDetails) {
    	fraudTransactionDetails.setRiskScore(encryptBigDecimal(fraudTransactionDetails.getRiskScore()));
    	return fraudTransactionDetails;
    }


    /**
     * Encrypts a Long value using XOR and bit rotation with a specified key.
     * 
     * @param val The plaintext Long value.
     * @return The encrypted Long value.
     */
    public Long encryptLong(Long val) {
        byte[] keyBytes = encryptionKey.getBytes();
        long encrypted = val;

        for (int i = 0; i < keyBytes.length; i++) {
            encrypted ^= keyBytes[i];
            encrypted = Long.rotateLeft(encrypted, 8);
        }

        return encrypted;
    }

    /**
     * Encrypts a BigDecimal value by converting it to a long, encrypting it, 
     * and converting it back to BigDecimal.
     * 
     * @param val The plaintext BigDecimal value.
     * @return The encrypted BigDecimal value.
     */
    public BigDecimal encryptBigDecimal(BigDecimal val) {
        if (val == null) {
            return null;
        }

        // Convert BigDecimal to BigInteger and preserve the scale
        BigInteger unscaledValue = val.unscaledValue();
        int scale = val.scale();

        // Get the encryption key bytes
        byte[] keyBytes = encryptionKey.getBytes();

        // Encrypt the BigInteger
        BigInteger encrypted = unscaledValue;
        for (byte keyByte : keyBytes) {
            encrypted = encrypted.add(BigInteger.valueOf(keyByte & 0xFF));
            encrypted = encrypted.shiftLeft(8); // Rotate bits
        }

        // Return as BigDecimal with original scale
        return new BigDecimal(encrypted, scale);
    }

    /**
     * Encrypts a String value by applying XOR with the encryption key and encoding in Base64.
     * 
     * @param val The plaintext String value.
     * @return The encrypted String value in Base64 format.
     */
    public String encrypt(String val) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < val.length(); i++) {
            char actualChar = val.charAt(i);
            char keyChar = encryptionKey.charAt(i % encryptionKey.length());
            result.append((char) (actualChar ^ keyChar));
        }

        return Base64.getEncoder().encodeToString(result.toString().getBytes(StandardCharsets.UTF_8));
    }
}
