package com.lti.model;

import com.lti.model.Status;

//just to check the working ...this will be edited later on
public class RegisterStatus extends Status {

	private long referenceId;

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long registeredCustomerId) {
		this.referenceId = registeredCustomerId;
	}
	
}