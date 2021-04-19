//The model holds application data, which is displayed in the view. 
package com.lti.model;

public class Status {

	private String message;
	private boolean status;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
