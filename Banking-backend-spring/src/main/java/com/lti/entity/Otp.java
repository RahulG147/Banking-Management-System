package com.lti.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_otp")
public class Otp{
	
	@Id
	private int otpId;
	
	private String otp;
	private LocalDateTime dateTime;
	
	@OneToOne
	@JoinColumn(name="from_account")
	private AccountDetail fromAccount;
	
	public AccountDetail getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(AccountDetail fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
