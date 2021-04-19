package com.lti.entity;

import java.io.Serializable;
import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_general_detail")
public class GeneralDetail implements Serializable{

	@Id
	@Column(name="aadhaar_card")
	private long aadhaarNo;
	
	@OneToOne
	@JoinColumn(name="customer_id")
	private Account account;
	
	@Column(name="customer_name")
	private String fullName;
	
	@Column(name="pan")
	private String panCard;
	
	@Column(name="dob")
	private LocalDate dateOfBirth;
	
	@Column(name="mailing_address")
	private String mailingAddress;
	
	@Column(name="occupation")
	private String occupation;
	
	
	@Column(name="gross_income")
	private double grossIncome;
	
	@Column(name="revenue_register_no")
	private String revenueRegisterNo;
	
	@Column(name="gst_number")
	private String gstNumber;
	
	public String getRevenueRegisterNo() {
		return revenueRegisterNo;
	}
	public void setRevenueRegisterNo(String revenueRegisterNo) {
		this.revenueRegisterNo = revenueRegisterNo;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	
	public String getPanCard() {
		return panCard;
	}
	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getMailingAddress() {
		return mailingAddress;
	}
	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public long getAadhaarNo() {
		return aadhaarNo;
	}
	public void setAadhaarNo(long aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}
	public double getGrossIncome() {
		return grossIncome;
	}
	public void setGrossIncome(double grossIncome) {
		this.grossIncome = grossIncome;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
	
	
	
}
