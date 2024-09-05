/**
 * @author: Naren
 * @date: 31/08/2024
 * 
 * @description:
 * CustomErrorMessage represents a standardized error response format for handling exceptions.
 * 
 * It contains:
 * - `status`: The HTTP status code associated with the error.
 * - `error`: A descriptive message detailing the error encountered.
 * 
 * This class is used to provide consistent error responses across the application.
 */


package com.ezpay.exception;

//import org.springframework.http.HttpStatus;

public class CustomErrorMessage {
	
	private int status;
	private String error;
 
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
