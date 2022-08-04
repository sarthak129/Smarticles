package com.asdc.smarticle.user.exception;
/**
 * Custom exception to notify is user is registered with the given email id and username.
 * 
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-02-19
 */
public class UserExistException extends Exception {

	private String message;

	public UserExistException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
