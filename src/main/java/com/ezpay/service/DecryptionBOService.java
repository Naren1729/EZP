/**
 * @author: Keerthana
 * @date: 03/09/2024
 * 
 * @description:
 * DecryptionBOService handles the decryption of sensitive data for various entities.
 * 
 * This service provides methods to decrypt user information, transaction details,
 * and fraud transaction details. It also includes decryption for various data types
 * including String, BigDecimal, Long, and Double using a predefined encryption key.
 * 
 * The service is annotated with @Service to indicate it is a Spring-managed service component.
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
    public class DecryptionBOService implements DecryptionInterface {

        // Predefined encryption key used for decryption
        private final String encryptionKey = "KEY";

        @Autowired
        public UserRepo userRepo;


        /**
         * Decrypts transaction details including amount, transaction type, and status.
         * 
         * @param transactionDetails The TransactionDetails object containing encrypted data.
         * @return The TransactionDetails object with decrypted data.
         */
        public TransactionDetails decryptTransaction(TransactionDetails transactionDetails) {
            transactionDetails.setAmount(decryptBigDecimal(transactionDetails.getAmount()));
            transactionDetails.setDestinationUserId(transactionDetails.getDestinationUserId());
            transactionDetails.setUsernId(transactionDetails.getUsernId());
            transactionDetails.setTransactionType(decrypt(transactionDetails.getTransactionType()));
            transactionDetails.setTransactionStatus(decrypt(transactionDetails.getTransactionStatus()));
            return transactionDetails;
        }

        /**
         * Decrypts fraudulent transaction details including the associated transaction and risk score.
         * 
         * @param fraudTransaction The FraudTransactionDetails object containing encrypted data.
         * @return The FraudTransactionDetails object with decrypted data.
         */
        public FraudTransactionDetails decryptFraud(FraudTransactionDetails fraudTransaction) {
            fraudTransaction.setRiskScore(decryptBigDecimal(fraudTransaction.getRiskScore())); // Assuming riskScore remains unchanged
            fraudTransaction.setTransaction(decryptTransaction(fraudTransaction.getTransaction()));
            return fraudTransaction;
        }
        
        /**
         * Decrypts a Long value using bitwise operations and XOR with the encryption key.
         * 
         * @param val The encrypted Long value.
         * @return The decrypted Long value.
         */
        public Long decryptLong(Long val) {
            byte[] keyBytes = encryptionKey.getBytes();
            long decrypted = val;

            for (int i = keyBytes.length - 1; i >= 0; i--) {
                decrypted = Long.rotateRight(decrypted, 8); // Rotate bits back
                decrypted ^= keyBytes[i]; // XOR each byte of the key with the encrypted value
            }

            return decrypted;
        }


        /**
         * Decrypts a BigDecimal value by processing its unscaled value with bitwise operations and XOR.
         * 
         * @param val The encrypted BigDecimal value.
         * @return The decrypted BigDecimal value.
         */
        public BigDecimal decryptBigDecimal(BigDecimal val) {
            if (val == null) {
                return null;
            }

            // Get the scale and unscaled value of the BigDecimal
            int scale = val.scale();
            BigInteger encrypted = val.unscaledValue();

            // Get the encryption key bytes
            byte[] keyBytes = encryptionKey.getBytes();

            // Decrypt the BigInteger
            for (int i = keyBytes.length - 1; i >= 0; i--) {
                encrypted = encrypted.shiftRight(8); // Rotate bits
                encrypted = encrypted.subtract(BigInteger.valueOf(keyBytes[i] & 0xFF));
            }

            // Return as BigDecimal with original scale
            return new BigDecimal(encrypted, scale);
        }

        /**
         * Decrypts a String value using Base64 decoding and XOR with the encryption key.
         * 
         * @param val The encrypted String value.
         * @return The decrypted String value.
         */
        public String decrypt(String val) {
            byte[] decodedBytes = Base64.getDecoder().decode(val);
            StringBuilder result = new StringBuilder();
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            for (int i = 0; i < decodedString.length(); i++) {
                char actualChar = decodedString.charAt(i);
                char keyChar = encryptionKey.charAt(i % encryptionKey.length());
                result.append((char) (actualChar ^ keyChar)); // XOR each character with the key character
            }

            return result.toString();
        }
    }
