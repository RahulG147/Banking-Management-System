package com.lti.compoundKey;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.lti.entity.AccountDetail;

//coumpound key
public class PayeeCompound implements Serializable {

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_account_no")
	private AccountDetail userAccount;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "beneficiary_account_no")
	private AccountDetail beneficiaryAccount;

	

	public AccountDetail getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(AccountDetail userAccount) {
		this.userAccount = userAccount;
	}

	public AccountDetail getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(AccountDetail beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}
	
	
	
	
}
