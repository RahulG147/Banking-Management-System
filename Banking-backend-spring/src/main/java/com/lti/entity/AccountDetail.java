package com.lti.entity;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lti.enums.AccountType;

@Entity
@Table(name="tbl_account_type")
public class AccountDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_seq3")
	@SequenceGenerator(sequenceName = "reference_seq3", initialValue = 1000, allocationSize = 1, name="new_seq3")
	@Column(name="account_number")
	private long accountNumber;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Account account;
	
	@Column(name="account_type")
	private AccountType accountType;
	
	@Column(name="balance")
	private double bankBalance;
	
	@OneToMany(mappedBy = "fromAccount")
	private List<Transaction> fromTransactions;
	
	@OneToMany(mappedBy = "toAccount")
	private List<Transaction> toTransactions;
	
	@OneToMany(mappedBy = "compoundKey.userAccount",cascade =CascadeType.ALL)
	private List<Payee> userKey;
	
	@OneToMany(mappedBy = "compoundKey.beneficiaryAccount",cascade =CascadeType.ALL)
	private List<Payee> beneficiaryKey;

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

	public List<Payee> getUserKey() {
		return userKey;
	}

	public void setUserKey(List<Payee> userKey) {
		this.userKey = userKey;
	}

	public List<Payee> getBeneficiaryKey() {
		return beneficiaryKey;
	}

	public void setBeneficiaryKey(List<Payee> beneficiaryKey) {
		this.beneficiaryKey = beneficiaryKey;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setFromTransactions(List<Transaction> fromTransactions) {
		this.fromTransactions = fromTransactions;
	}

	public void setToTransactions(List<Transaction> toTransactions) {
		this.toTransactions = toTransactions;
	}

	public List<Transaction> getFromTransactions() {
		return fromTransactions;
	}

	public List<Transaction> getToTransactions() {
		return toTransactions;
	}
}
