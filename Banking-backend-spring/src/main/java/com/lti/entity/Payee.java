package com.lti.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.lti.compoundKey.PayeeCompound;



@Entity
@Table(name="tbl_payee")
public class Payee {

	@EmbeddedId
	@Column(name="beneficiary_id")
	private PayeeCompound compoundKey;
	
	@Column(name="beneficiary_name")
	private String beneficiaryName;
	
	@Column(name="nick_name")
	private String nickName;

	public PayeeCompound getCompoundKey() {
		return compoundKey;
	}

	public void setCompoundKey(PayeeCompound compoundKey) {
		this.compoundKey = compoundKey;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
	
	
	
	
}
