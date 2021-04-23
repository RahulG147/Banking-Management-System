package com.lti.model;

import com.lti.enums.AccountType;

public class AccountSummary extends Status {
	
	private long accountNumber;
	private AccountType accountType;
	private double bankBalance;


	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public double getBankBalance() {
		return bankBalance;
	}

	public void setBankBalance(double bankBalance) {
		this.bankBalance = bankBalance;
	}
	
	
}
