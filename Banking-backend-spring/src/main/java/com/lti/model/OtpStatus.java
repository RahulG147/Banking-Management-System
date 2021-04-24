package com.lti.model;

import com.lti.entity.AccountDetail;

public class OtpStatus extends Status{

	private String otp;
	private long fromAccount;
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public long getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(long fromAccount) {
		this.fromAccount = fromAccount;
	}

	
}
