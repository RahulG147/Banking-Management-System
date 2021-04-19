package com.lti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lti.model.Login;
import com.lti.model.LoginStatus;
import com.lti.model.RegisterStatus;
import com.lti.model.TransactionStatus;
import com.lti.model.Transactions;
import com.lti.entity.Account;
import com.lti.entity.Registration;
import com.lti.service.CustomerService;
import com.lti.service.ServiceException;

@RestController
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/register")
	public RegisterStatus register(@RequestBody Registration customer) {
	
		try {
			long id = customerService.register(customer);
			RegisterStatus status = new RegisterStatus();
			status.setStatus(true);
			status.setMessage("Registration successfull!!!");
			status.setReferenceId(id);
			return status;
		}
		catch(ServiceException e) {
			RegisterStatus status = new RegisterStatus();
			status.setStatus(false);
			status.setMessage(e.getMessage());
			return status;
		}
	}
	
	@PostMapping("/userlogin")
	public LoginStatus login(@RequestBody Login login) {
		try {
			Account account = customerService.login(login.getCustomerId(), login.getLoginPassword());
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(true);
			loginStatus.setMessage("Login successful!");
			loginStatus.setCustomerId(account.getCustomerId());
			Registration registration = new  Registration();
			loginStatus.setName(registration.getFirstName());
			loginStatus.setName(registration.getMiddleName());
			loginStatus.setName(registration.getLastName());
			
			
			return loginStatus;
		}
		catch(ServiceException e) {
			LoginStatus loginStatus = new LoginStatus();
			loginStatus.setStatus(false);
			loginStatus.setMessage(e.getMessage());		
			return loginStatus;
		}
	}
	
	@PostMapping("/impstransaction")
	public TransactionStatus imps(@RequestBody Transactions transaction) {
		try {
			String referenceId = customerService.impsTransaction(transaction);
			
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(true);
			transactionStatus.setRefernceNo(referenceId);
			transactionStatus.setMessage("Amount has been debited from your account and will be credited to the receipent's account");
			
			return transactionStatus;
		}
		catch (ServiceException e) {
			TransactionStatus transactionStatus = new TransactionStatus();
			transactionStatus.setStatus(false);
			transactionStatus.setMessage(e.getMessage());
			return transactionStatus;
		}
	}
}
