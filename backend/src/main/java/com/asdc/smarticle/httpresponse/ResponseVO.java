package com.asdc.smarticle.httpresponse;

/**
* Http response model.
* @author  Vivekkumar Patel
* @version 1.0
* @since   2022-02-19
*/
public class ResponseVO<D> {

	private D data;
	private String message;
	private boolean status;
	private int statusCode;
	private String errorCode;

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) { this.message = message; }

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
