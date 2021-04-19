package com.lti.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lti.enums.Title;

@Entity
@Table(name="tbl_registration_detail")
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_seq1")
	@SequenceGenerator(sequenceName = "reference_seq1", initialValue = 1, allocationSize = 1, name="new_seq1")	
	@Column(name="service_reference_no")
	private long referenceNo;

	@Column(name="title")
	private Title title;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@Column(name="middle_name")
	private String middleName;

	@Column(name="father_name")
	private String fatherName;

	@Column(name="mobile_number")
	private long mobileNo;

	@Column(name="email_id")
	private String emailId;

	@Column(name="aadhar")
	private long aadhaarNo;

	@Column(name="pan")
	private String panCard;

	@Column(name="dob")
	private LocalDate dateOfBirth;

	@Column(name="residential_address")
	private String residentialAddress;

	@Column(name="permanent_address")
	private String permanent;

	@Column(name="occupation_type")
	private String occupation;

	@Column(name="income_source")
	private String incomeSource;

	@Column(name="revenue_register_no")
	private String revenueRegisterNo;
	
	@Column(name="gst_number")
	private String gstNumber;

	@Column(name="gross_income")
	private double annualIncome;

	@OneToOne(mappedBy = "registration",cascade = CascadeType.ALL)
	private Account account;
	
	
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public long getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(long referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public long getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(long aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
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

	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}

	public String getPermanent() {
		return permanent;
	}

	public void setPermanent(String permanent) {
		this.permanent = permanent;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(String incomeSource) {
		this.incomeSource = incomeSource;
	}

	public double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(double annualIncome) {
		this.annualIncome = annualIncome;
	}

	

}
