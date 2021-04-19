package com.lti.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lti.enums.TransactionType;

@Entity
@Table(name="tbl_transaction_detail")
public class Transaction implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_seq6")
	@SequenceGenerator(sequenceName = "reference_seq6", initialValue = 50000, allocationSize = 1, name="new_seq6")	
	@Column(name="transaction_id")
	private long transactionId;
	
	@ManyToOne
	@JoinColumn(name="from_account")
	private AccountDetail fromAccount;
	
	@ManyToOne
	@JoinColumn(name="to_account")
	private AccountDetail toAccount;
	
	@Column(name="tr_amount")
	private double amount;
	
	@Column(name="tr_mode")
	private TransactionType modeOfTransaction;
	
	@Column(name="transaction_date")
	private LocalDateTime transactionDate;
	
	@Column(name="tr_remarks")
	private String remarks;
	
	@Column(name="message")
	private String message;
	
	@Column(name="tr_status")
	private String status;
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public AccountDetail getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(AccountDetail fromAccount) {
		this.fromAccount = fromAccount;
	}
	public AccountDetail getToAccount() {
		return toAccount;
	}
	public void setToAccount(AccountDetail toAccount) {
		this.toAccount = toAccount;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public TransactionType getModeOfTransaction() {
		return modeOfTransaction;
	}
	public void setModeOfTransaction(TransactionType modeOfTransaction) {
		this.modeOfTransaction = modeOfTransaction;
	}
	
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
