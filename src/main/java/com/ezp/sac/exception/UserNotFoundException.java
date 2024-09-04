/** @Authors : Keerthana B
 * @Date : 31/08/2024
 * 
 * @Description:
 * Custom exception to handle scenarios where a user is not found in the system.
 * This exception extends RuntimeException and is used to signal that a user could
 * not be located based on the provided criteria.
 */
package com.ezp.sac.exception;


public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	// Default constructor for UserNotFoundException.
	public UserNotFoundException() {
		super();
	}

//    Constructor for UserNotFoundException with a custom message.
	public UserNotFoundException(String message) {
		super(message);
	}
}