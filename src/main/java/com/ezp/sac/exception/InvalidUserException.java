/**
 * @author: Naren
 * @date: 31/08/2024
 * 
 * @description:
 * InvalidUserException is a custom runtime exception used to indicate issues with user data.
 * 
 * It extends RuntimeException and provides two constructors:
 * - A default constructor.
 * - A constructor that accepts a custom error message.
 * 
 * This exception is thrown when user data is found to be invalid or incorrect.
 */


package com.ezp.sac.exception;
  
public class InvalidUserException extends RuntimeException {
  
	public InvalidUserException() {
		super(); 
	}
  
	public InvalidUserException(String message) { 
		super(message); 
	} 
}
 