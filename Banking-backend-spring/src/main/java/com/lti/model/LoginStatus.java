package com.lti.model;

import java.util.ArrayList;
import java.util.List;

public class LoginStatus extends Status{

	private long customerId;
	private String name;
	private List<Long> accounts=new ArrayList<Long>();
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Long> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Long> accounts) {
		this.accounts = accounts;
	}
	
	
	
	
	
}
