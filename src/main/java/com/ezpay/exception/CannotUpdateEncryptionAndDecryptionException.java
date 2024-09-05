/** @Authors : Keerthana B
* @Date : 31/08/2024
* 
* @Description:
* Custom exception to handle scenarios where encryption and decryption updates fails to
* get updated back into the database. This exception extends RuntimeException and 
* can be used to signal errors specifically related to encryption and decryption 
* operations in the application.
*/

package com.ezpay.exception;

public class CannotUpdateEncryptionAndDecryptionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

//	Default Constructor for EncryptionOrDecryptionException.
	public CannotUpdateEncryptionAndDecryptionException() {
	}

//	Constructor for EncryptionOrDecryptionException with a custom message.
	public CannotUpdateEncryptionAndDecryptionException(String message) {
		super(message);
	}

}
