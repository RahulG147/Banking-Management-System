package com.lti.model;

public class Credential extends Status{

	private long serviceNumber;
	private long customerId;
	private long accountNumber;
	private String loginPassword;
	private String TransactionPassword;
	public long getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(long serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getTransactionPassword() {
		return TransactionPassword;
	}
	public void setTransactionPassword(String transactionPassword) {
		TransactionPassword = transactionPassword;
	}
	
	
	
}
