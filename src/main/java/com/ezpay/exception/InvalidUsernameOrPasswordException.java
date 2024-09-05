/** @Authors : Keerthana B
 * @Date : 31/08/2024
 * 
 * @Description:
 * Custom exception to handle scenarios where user authentication fails due to
 * invalid username or password. This exception extends RuntimeException and is
 * used to signal errors specifically related to user authentication issues.
 */

package com.ezpay.exception;

public class InvalidUsernameOrPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

//	Default constructor for InvalidUserbameOrPasswordException.
	public InvalidUsernameOrPasswordException() {
	}

//	Constructor for InvalidUserbameOrPasswordException with a custom message.
	public InvalidUsernameOrPasswordException(String message) {
		super(message);
	}

}
