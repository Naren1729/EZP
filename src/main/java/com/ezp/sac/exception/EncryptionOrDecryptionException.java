/** @Authors : Keerthana B
 * @Date : 31/08/2024
 * 
 * @Description:
 * Custom exception to handle scenarios where encryption or decryption operations fail.
 * This exception extends RuntimeException and is used to signal errors specifically related
 * to encryption and decryption processes within the application.
 */

package com.ezp.sac.exception;

public class EncryptionOrDecryptionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

//	Default constructor for EncryptionOrDecryptionException.
	public EncryptionOrDecryptionException() {
	}

//	Constructor for EncryptionOrDecryptionException with a custom message.
	public EncryptionOrDecryptionException(String message) {
		super(message);
	}

}
