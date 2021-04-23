package com.lti.model;

import com.lti.entity.AccountDetail;

public class NewBeneficiaryStatus extends Status{

	long beneficiaryAccountNumber;

	public long getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}

	public void setBeneficiaryAccountNumber(long beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
	}

	

	
}
