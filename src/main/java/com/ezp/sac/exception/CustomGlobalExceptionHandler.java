/** @Authors : Keerthana B, Naren
 * @Date : 31/08/2024
 * 
 * @Description:
 * Global exception handler for the application. This class handles various exceptions
 * that occur in the application and returns a standardised error response.
 * It extends ResponseEntityExceptionHandler to provide custom error responses.
 */

package com.ezp.sac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import com.ezp.sac.controller.InvalidUserbameOrPasswordException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	/**
	 * Handles InvalidUserbameOrPasswordException and returns a BAD_REQUEST
	 * response.
	 * 
	 * @param ex      the exception
	 * @param request the request information
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(InvalidUserbameOrPasswordException.class)
	public ResponseEntity<CustomErrorMessage> handleInvalidUserbameOrPasswordException(
			InvalidUserbameOrPasswordException ex, WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage();
		customErrorMessage.setError(ex.getMessage());
		customErrorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<CustomErrorMessage>(customErrorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CannotUpdateEncryptionAndDecryptionException.class)
	public ResponseEntity<CustomErrorMessage> handleCannotUpdateEncryptionException(
			CannotUpdateEncryptionAndDecryptionException ex, WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage();
		customErrorMessage.setError(ex.getMessage());
		customErrorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<CustomErrorMessage>(customErrorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EncryptionOrDecryptionException.class)
	public ResponseEntity<CustomErrorMessage> handleEncryptionException(EncryptionOrDecryptionException ex,
			WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage();
		customErrorMessage.setError(ex.getMessage());
		customErrorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<CustomErrorMessage>(customErrorMessage, HttpStatus.BAD_REQUEST);
	} // KEERTHANA

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<CustomErrorMessage> handleUserNotFoundException(UserNotFoundException ex,
			WebRequest request) {
		CustomErrorMessage customErrorMessage = new CustomErrorMessage();
		customErrorMessage.setError(ex.getMessage());
		customErrorMessage.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(customErrorMessage, HttpStatus.NOT_FOUND);
	}

	
	  @ExceptionHandler(InvalidUserException.class) public ResponseEntity<CustomErrorMessage> handleInvalidUserException(InvalidUserException ex, WebRequest request) {
		  CustomErrorMessage customErrorMessage = new CustomErrorMessage();
		  customErrorMessage.setError(ex.getMessage());
		  customErrorMessage.setStatus(HttpStatus.BAD_REQUEST.value()); 
		  return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST); 
	  }
	 

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomErrorMessage> handleRuntimeException(RuntimeException ex, WebRequest request) {
        CustomErrorMessage errors = new CustomErrorMessage();
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
