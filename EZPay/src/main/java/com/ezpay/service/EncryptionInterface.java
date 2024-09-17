/**@author: Keerthana
 * @date: 2/09/2024
 * 
 * @description:
 * EncryptionInterface defines methods for encrypting various data types and entities.
 * 
 * This interface provides a contract for encrypting user information, transaction details,
 * and other data types such as double, Long, BigDecimal, and String. Implementations of this
 * interface should provide specific encryption logic for these data types and entities.
 */

package com.ezpay.service;

import java.math.BigDecimal;

import com.ezpay.entity.TransactionDetails;
import com.ezpay.entity.User;


public interface EncryptionInterface {

    /**
     * Encrypts the details of a User object.
     * 
     * @param user The User object containing data to be encrypted.
     * @return The User object with encrypted data.
     */
//    public User encryptUser(User user);

    /**
     * Encrypts the details of a TransactionDetails object.
     * 
     * @param transactionDetails The TransactionDetails object containing data to be encrypted.
     * @return The TransactionDetails object with encrypted data.
     */
    public TransactionDetails encryptTransaction(TransactionDetails transactionDetails);

    /**
     * Encrypts a Long value.
     * 
     * @param val The Long value to be encrypted.
     * @return The encrypted Long value.
     */
    public Long encryptLong(Long val);

    /**
     * Encrypts a BigDecimal value.
     * 
     * @param val The BigDecimal value to be encrypted.
     * @return The encrypted BigDecimal value.
     */
    public BigDecimal encryptBigDecimal(BigDecimal val);

    /**
     * Encrypts a String value.
     * 
     * @param val The String value to be encrypted.
     * @return The encrypted String value.
     */
    public String encrypt(String val);
}
