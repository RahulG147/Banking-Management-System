package com.lti.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_credential_detail")
public class AccountCredential {

	@Id
	@Column(name="reference_no")
	private long registration ;
	
	@Column(name="login_pwd")
	private String loginPassword;
	
	@Column(name="transaction_pwd")
	private String transactionPassword;
	
	@Column(name="acc_no")
	private long accountNumber;
	
	@Column(name="balance")
	private double balance;
	
	@Column(name="acc_type")
	private String accountType;
	
	@Column(name="cust_no")
	private long customerId;
	
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getRegistration() {
		return registration;
	}
	public void setRegistration(long registration) {
		this.registration = registration;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getTransactionPassword() {
		return transactionPassword;
	}
	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	
}
